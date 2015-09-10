package visualization;

import graph.CanvasPosition;
import graph.Graph;
import graph.Vertex;
import util.Action;
import util.FileOperations;
import util.MalformedGraphException;
import util.MouseMode;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Drawer extends JPanel implements MouseMotionListener, MouseListener, KeyListener {
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
        frame.addKeyListener(this);
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

        CursorManager.setCursor(frame, MouseMode.VERTEX);
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
        synchronized (buttons) {
            buttons.clear();
            // Get the dimensions of the window border to calculate accurate coordinates on the
            // screen

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
            final ScreenPosition connectionButtonPosition = new ScreenPosition(
                    frame.getWidth() - left -
                            100, frame.getHeight() - top - 50);
            final ModeButton connectionButton = new ModeButton(MouseMode.CONNECTION,
                    connectionButtonPosition, 100, 50, "Connection");
            buttons.add(connectionButton);
            final ScreenPosition removeButtonPosition = new ScreenPosition(frame.getWidth() - left -
                    300, frame.getHeight() - top - 50);
            final ModeButton removeButton = new ModeButton(MouseMode.REMOVE, removeButtonPosition,
                    100, 50, "Remove");
            buttons.add(removeButton);

            final ScreenPosition saveButtonPosition = new ScreenPosition(0, frame.getHeight() -
                    top -
                    50);
            final ActionButton saveButton = new ActionButton(saveButtonPosition, 100, 50, "Save",
                    Action.SAVE);
            buttons.add(saveButton);
            final ScreenPosition loadButtonPosition = new ScreenPosition(100,
                    frame.getHeight() - top - 50);
            final ActionButton loadButton = new ActionButton(loadButtonPosition, 100, 50, "Load",
                    Action.LOAD);
            buttons.add(loadButton);
            final ScreenPosition resetButtonPosition = new ScreenPosition(200, frame.getHeight() -
                    top - 50);
            final ActionButton resetButton = new ActionButton(resetButtonPosition, 100, 50, "Reset",
                    Action.RESET);
            buttons.add(resetButton);
        }
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

        if (mode == MouseMode.VERTEX) {
            boolean drawGhost = true;
            synchronized (buttons) {
                for (Button button : buttons) {
                    if (button.contains(new ScreenPosition(mouseX, mouseY))) {
                        drawGhost = false;
                        break;
                    }
                }
            }
            for (Vertex vertex : graph.getVertices()) {
                if (vertex.pointInVertex(getCanvasPosition(mouseX, mouseY))) {
                    drawGhost = false;
                    break;
                }
            }

            if (drawGhost) {
                Vertex.drawGhost(g, mouseX, mouseY);
            }
        }

        if (graph != null) {
            graph.drawGraph(g, canvasX, canvasY);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.setColor(frame.getBackground());
            g.fillRect(0, 0, 200, 230);
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, 230, 200, 230);
            g.drawLine(200, 0, 200, 230);
            g.setColor(Color.white);
            g.drawString("Number of Vertices: " + graph.numberOfVertices(), 10, 20);
            //TODO For directed graphs don't divide by two
            g.drawString("Number of Connections: " + graph.numberOfConnections() / 2, 10, 40);
            g.drawString("Number of Colors: " + graph.numberOfColors(), 10, 60);
            g.drawString("Maximum Degree: " + graph.properties().getMaxDegree(), 10, 80);
            g.drawString("Bipartite: " + graph.properties().isBipartite(), 10, 100);
            g.drawString("Connected: " + graph.properties().isConnected(), 10, 120);
            g.drawString("Tree: " + graph.properties().isTree(), 10, 140);
            g.drawString("Regular: " + graph.properties().isRegular(), 10, 160);
            g.drawString("Complete: " + graph.properties().isComplete(), 10, 180);
            g.drawString("Number of Cycles: " + graph.properties().numCycles(), 10, 200);
            g.drawString("Girth: " + graph.properties().getGirth(), 10, 220);

            synchronized (buttons) {
                for (Button b : buttons) {
                    b.draw(g);
                }
            }

            if (selectedVertex == null && infoNode != null) {
                calculateBestInfoPanelPosition();
                infoPanel.draw(g);
            }
        }
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
                selectedVertex.setPosition(getCanvasPosition(e));
            }
        }

        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        updateMousePosition(e);

        // Update the buttons to reflect whether the mouse is over them
        for (Button button : buttons) {
            // If the button is not already selected and the mouse is over it put it in the hover
            // state
            if (button.getButtonState() != ButtonState.SELECTED && button.contains(
                    getScreenPosition(e))) {
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
                        changeMouseMode(((ModeButton) button).getMode());
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

        // TODO Don't go through every node but instead set it above
        // Update the vertex to draw the info panel for
        infoNode = null;
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.pointInVertex(getCanvasPosition(e))) {
                infoNode = vertex;
                break;
            }
        }

        repaint();
    }

    private void calculateBestInfoPanelPosition() {
        if (infoNode != null) {
            infoPanel.setVertex(infoNode);

            int fewestConflicts = Integer.MAX_VALUE;
            Diagonal bestDiagonal = Diagonal.LOWER_RIGHT;
            for (final Diagonal diagonal : Diagonal.values()) {
                final RectangleOnScreen rectangle = infoPanel.getRectangle(diagonal);
                final int conflicts = calculateConflicts(rectangle);
                if (conflicts < fewestConflicts) {
                    bestDiagonal = diagonal;
                    fewestConflicts = conflicts;
                }
            }

            infoPanel.setDrawDirection(bestDiagonal);
        }
    }

    private int calculateConflicts(RectangleOnScreen rectangle) {
        int conflicts = 0;
        for (final Vertex vertex : graph.getVertices()) {
            if (rectangle.containsVertex(vertex, canvasX, canvasY)) {
                conflicts++;
            }
        }
        return conflicts;
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

    private CanvasPosition getCanvasPosition(int x, int y) {
        return new CanvasPosition(x - canvasX, y - canvasY);
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
        final int response = JOptionPane.showConfirmDialog(null,
                "Do you want to reset the graph? This action is not undoable.", "Confirm Reset",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            graph = new Graph();
            infoNode = null;
            selectedVertex = null;

            canvasX = 0;
            canvasY = 0;
            dragged = false;
            draggingCanvas = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_S:
                    FileOperations.saveFile();
                    break;
                case KeyEvent.VK_L:
                    FileOperations.loadFile();
                    break;
                case KeyEvent.VK_N:
                    reset();
                    break;
                case KeyEvent.VK_C:
                    changeMouseMode(MouseMode.CONNECTION);
                    break;
                case KeyEvent.VK_V:
                    changeMouseMode(MouseMode.VERTEX);
                    break;
                case KeyEvent.VK_R:
                    changeMouseMode(MouseMode.REMOVE);
                    break;
            }
        } catch (IOException | MalformedGraphException e1) {
            e1.printStackTrace();
        } finally {
            repaint();
        }
    }

    private void changeMouseMode(MouseMode newMouseMode) {
        mode = newMouseMode;
        selectButton(getButton(mode));
        CursorManager.setCursor(frame, newMouseMode);
    }

    private void selectButton(Button button) {
        // Deselect the currently selected button if there is one
        if (selectedButton != null) {
            selectedButton.setButtonState(ButtonState.NORMAL);
        }
        // Set the new button as selected
        button.setButtonState(ButtonState.SELECTED);
        selectedButton = button;
    }

    private Button getButton(MouseMode mode) {
        for (Button button : buttons) {
            if (button instanceof ModeButton && ((ModeButton) button).getMode() == mode) {
                return button;
            }
        }

        return null;
    }
}