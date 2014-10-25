package visualization;

import graph.CanvasPosition;
import graph.Graph;
import graph.Vertex;
import util.Action;
import util.MouseMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Drawer extends JPanel implements MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 5174812665272092921L;

    private static final double VERSION = 0.1;

    private static JFrame frame;
    private static Graph graph = new Graph();
    private static Vertex selectedVertex = null;
    private static Vertex infoNode = null;
    private static final InfoPanel infoPanel = new InfoPanel();

    private static boolean dragged = false;
    private static boolean draggingCanvas = false;
    private static long dragTimer = -1;
    private static int canvasX = 0;
    private static int canvasY = 0;
    private static int originalCanvasX, originalCanvasY;
    private static int mouseX, mouseY;
    private static int originalMouseX, originalMouseY;

    private static MouseMode mode = MouseMode.VERTEX;
    private static Button selectedButton;
    private static final ArrayList<Button> buttons = new ArrayList<>();
    private static final Font drawFont = new Font("Arial", Font.BOLD, 16);

    public Drawer() {
        frame = new JFrame("Graphinator v" + VERSION);
        frame.setVisible(true);
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
        // Reposition the buttons when resizing the window
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                initButtons();
                repaint();
            }
        });
        frame.add(this);
    }

    public static void main(String args[]) {
        new Drawer();
        initButtons();
    }

    /**
     * Returns the frame of the application.
     *
     * @return the frame
     */
    public static JFrame getFrame() {
        return frame;
    }

    /**
     * Returns the current graph of the application.
     *
     * @return the graph
     */
    public static Graph getGraph() {
        return graph;
    }

    /**
     * Returns the vertex to display information about.
     *
     * @return the vertex
     */
    public static Vertex getInfoNode() {
        return infoNode;
    }

    public static void initButtons() {
        buttons.clear();
        // Get the dimensions of the window border to calculate accurate coordinates on the screen
        final int left = frame.getInsets().left;
        final int top = frame.getInsets().top;

        final ScreenPosition vertexButtonPosition = new ScreenPosition(frame.getWidth() - left -
                200, frame.getHeight() - top - 50);
        final ModeButton vertexButton = new ModeButton(MouseMode.VERTEX, vertexButtonPosition,
                100, 50, "Vertex");
        buttons.add(vertexButton);
        //The vertex button starts out as the selected button
        selectedButton = vertexButton;
        vertexButton.setButtonState(ButtonState.SELECTED);
        final ScreenPosition connectionButtonPosition = new ScreenPosition(frame.getWidth() - left -
                100, frame.getHeight() - top - 50);
        final ModeButton connectionButton = new ModeButton(MouseMode.CONNECTION,
                connectionButtonPosition, 100, 50, "Connection");
        buttons.add(connectionButton);
        final ScreenPosition removeButtonPosition = new ScreenPosition(frame.getWidth() - left -
                300, frame.getHeight() - top - 50);
        final ModeButton removeButton = new ModeButton(MouseMode.REMOVE, removeButtonPosition,
                100, 50, "Remove");
        buttons.add(removeButton);

        final ScreenPosition saveButtonPosition = new ScreenPosition(0,
                frame.getHeight() - top - 50);
        final ActionButton saveButton = new ActionButton(saveButtonPosition, 100, 50, "Save",
                Action.SAVE);
        buttons.add(saveButton);
        final ScreenPosition loadButtonPosition = new ScreenPosition(100,
                frame.getHeight() - top - 50);
        final ActionButton loadButton = new ActionButton(loadButtonPosition, 100, 50, "Load",
                Action.LOAD);
        buttons.add(loadButton);
    }

    public void paintComponent(Graphics g) {
        g.setFont(drawFont);
        g.setColor(frame.getBackground());
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        g.setColor(Color.white);
        // If the user is dragging a new connection show the connection preview
        if (mode == MouseMode.CONNECTION && selectedVertex != null) {
            g.drawLine(selectedVertex.getX() + canvasX, selectedVertex.getY() + canvasY, mouseX,
                    mouseY);
        }

        if (graph != null) {
            graph.drawGraph(g, canvasX, canvasY);
            g.setColor(frame.getBackground());
            g.fillRect(0, 0, 200, 150);
            g.setColor(Color.white);
            g.drawString("Number of Vertices: " + graph.numberOfVertices(), 10, 20);
            //TODO For directed graphs don't divide by two
            g.drawString("Number of Connections: " + graph.numberOfConnections() / 2, 10, 40);
            g.drawString("Number of Colors: " + graph.numberOfColors(), 10, 60);
            g.drawString("Maximum Degree: " + graph.properties().getMaxDegree(), 10, 80);
            g.drawString("Bipartite: " + graph.properties().isBipartite(), 10, 100);
            g.drawString("Connected: " + graph.properties().isConnected(), 10, 120);
            g.drawString("Tree: " + graph.properties().isTree(), 10, 140);
        }

        for (Button b : buttons) {
            b.draw(g);
        }

        infoPanel.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        updateMousePosition(e);
        final ScreenPosition screenPosition = getScreenPosition(e);

        // If we are dragging the canvas update the position of the drag
        if (draggingCanvas) {
            infoNode = null;
            canvasX = originalCanvasX - (originalMouseX - screenPosition.getX());
            canvasY = originalCanvasY - (originalMouseY - screenPosition.getY());
        }

        // If we have not started dragging the mouse add a delay to prevent unintended dragging
        // by the user
        if (!dragged) {
            if (dragTimer == -1) {
                dragTimer = 5;
            } else {
                dragTimer--;
                if (dragTimer == 0) {
                    dragged = true;
                }
            }
        }

        // If we are in vertex mode update the position of the selected vertex
        if (mode == MouseMode.VERTEX) {
            if (selectedVertex != null) {
                infoPanel.setPosition(getScreenPosition(e));
                selectedVertex.setPosition(getCanvasPosition(e));
            }
        }

        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        // Update the buttons to reflect whether the mouse is over them
        for (Button button : buttons) {
            // If the button is not already selected and the mouse is over it put it in the hover
            // state
            if (button.getButtonState() != ButtonState.SELECTED && button.contains
                    (getScreenPosition(e))) {
                button.setButtonState(ButtonState.HOVER);
            } else if (button.getButtonState() == ButtonState.HOVER) {
                button.setButtonState(ButtonState.NORMAL);
            }
        }

        // Highlight any vertices and connections that are within the removal range
        if (mode == MouseMode.REMOVE) {
            graph.highlightRemovals(getCanvasPosition(e));
        }

        // Update the vertex to draw the info panel for
        infoNode = null;
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.pointInVertex(getCanvasPosition(e))) {
                infoNode = vertex;
                infoPanel.setPosition(getScreenPosition(e));
                break;
            }
        }

        repaint();
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
        updateMousePosition(e);

        // Left-mouse button
        if (e.getButton() == MouseEvent.BUTTON1) {
            // See if any of the buttons were clicked
            for (Button button : buttons) {
                if (button.contains(getScreenPosition(e))) {
                    // Only ModeButtons should be selected
                    if (button instanceof ModeButton) {
                        // Deselect the currently selected button if there is one
                        if (selectedButton != null) {
                            selectedButton.setButtonState(ButtonState.NORMAL);
                        }
                        // Set the new button as selected
                        button.setButtonState(ButtonState.SELECTED);
                        selectedButton = button;
                    }

                    // Perform the button action if it has one
                    try {
                        button.perform();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    repaint();
                    return;
                }
            }

            if (mode == MouseMode.VERTEX) {
                for (Vertex vertex : graph.getVertices()) {
                    // Do not allow creating vertices on top of each other
                    if (vertex.pointInVertex(getCanvasPosition(e))) {
                        // The vertex may be dragged to alter its position
                        selectedVertex = vertex;
                        vertex.select();
                        return;
                    }
                }
                graph.createVertex(getCanvasPosition(e));
            } else if (mode == MouseMode.CONNECTION) {
                // See if we are creating a connection between vertices
                for (Vertex vertex : graph.getVertices()) {
                    if (vertex.pointInVertex(getCanvasPosition(e))) {
                        // Set the vertex as the starting vertex for the connection
                        selectedVertex = vertex;
                        vertex.select();
                        return;
                    }
                }
            } else if (mode == MouseMode.REMOVE) {
                // Remove the vertices and connections that are within range
                graph.remove(getCanvasPosition(e));
            }


        } else if (e.getButton() == MouseEvent.BUTTON3) {
            //Right-mouse is used to drag the canvas around
            originalMouseX = mouseX;
            originalMouseY = mouseY;
            originalCanvasX = canvasX;
            originalCanvasY = canvasY;
            draggingCanvas = true;
        }

        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        updateMousePosition(e);

        if (e.getButton() == MouseEvent.BUTTON1) {
            // If the mouse release came after dragging the mouse
            if (dragged) {
                if (mode == MouseMode.VERTEX) {
                    // Deselect the vertex that was being dragged
                    if (selectedVertex != null) {
                        selectedVertex.deselect();
                        selectedVertex = null;
                    }
                } else if (mode == MouseMode.CONNECTION) {
                    // If we were dragging a connection from a vertex
                    if (selectedVertex != null) {
                        for (Vertex vertex : graph.getVertices()) {
                            // Skip the selected vertex as we don't want to have self-loops
                            if (vertex.equals(selectedVertex)) {
                                continue;
                            }
                            // If the cursor is within the vertex toggle a connection
                            if (vertex.pointInVertex(getCanvasPosition(e))) {
                                graph.toggleConnection(selectedVertex, vertex);
                                //TODO do not perform this for directed graphs
                                graph.toggleConnection(vertex, selectedVertex);

                                selectedVertex = null;
                                graph.removeHighlights();
                                repaint();
                                return;
                            }
                        }
                        // If there is no vertex to create the connection with terminate the
                        // connection
                        if (selectedVertex != null) {
                            selectedVertex.deselect();
                            selectedVertex = null;
                        }
                    }
                }
            }
            // We are no longer dragging
            dragged = false;
            dragTimer = -1;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            // We are no longer dragging
            draggingCanvas = false;
        }

        repaint();
    }

    /**
     * Returns the position of the mouse represented by the given mouse event on the canvas. This
     * position is affected by scrolling and is used by the graph to represent the true
     * coordinates of vertices.
     *
     * @param e the mouse event
     *
     * @return the canvas position
     */
    private CanvasPosition getCanvasPosition(MouseEvent e) {
        final ScreenPosition screenPosition = getScreenPosition(e);
        final int x = screenPosition.getX() - canvasX;
        final int y = screenPosition.getY() - canvasY;

        return new CanvasPosition(x, y);
    }

    /**
     * Returns the position of the mouse represented by the given mouse event on the screen. This
     * position is used to draw elements on screen.
     *
     * @param e the mouse event
     *
     * @return the screen position
     */
    private ScreenPosition getScreenPosition(MouseEvent e) {
        final int x = e.getX() - frame.getInsets().left;
        final int y = e.getY() - frame.getInsets().top;

        return new ScreenPosition(x, y);
    }

    private void updateMousePosition(MouseEvent e) {
        final ScreenPosition screenPosition = getScreenPosition(e);

        mouseX = screenPosition.getX();
        mouseY = screenPosition.getY();
    }

    /**
     * Sets the mouse mode of the program.
     *
     * @param mode the mouse mode
     */
    public static void setMouseMode(MouseMode mode) {
        Drawer.mode = mode;
    }

    /**
     * Resets the graph by removing all vertices and connections.
     */
    public static void reset() {
        graph = new Graph();
        infoNode = null;
        selectedVertex = null;

        canvasX = 0;
        canvasY = 0;
        dragged = false;
        draggingCanvas = false;
    }
}