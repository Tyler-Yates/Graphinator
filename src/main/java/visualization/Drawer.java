package visualization;

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

    public static JFrame frame;
    public static Graph graph = new Graph();
    public static Vertex selectedVertex = null;
    static Vertex infoNode = null;
    static InfoPanel info = new InfoPanel();

    static long dragTimer = -1;

    static int canvasX, canvasY;

    static int mouseX, mouseY;

    static boolean draggingCanvas = false;
    static int originalCanvasX, originalCanvasY;
    static int originalMouseX, originalMouseY;

    public static MouseMode mode = MouseMode.VERTEX;
    private static Button selectedButton;

    static ArrayList<Button> buttons = new ArrayList<>();

    static boolean dragged = false;

    static Font drawFont = null;

    static boolean isConnected = true;
    static boolean isTree = true;

    public Drawer() {
        frame = new JFrame("Graphinator v" + VERSION);
        frame.setVisible(true);
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
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
        canvasX = canvasY = 0;
        initButtons();

        drawFont = new Font("Arial", Font.BOLD, 16);
    }

    public static void initButtons() {
        buttons.clear();
        int x = frame.getInsets().left;
        int y = frame.getInsets().top;
        ModeButton vertexButton = new ModeButton(MouseMode.VERTEX, frame.getWidth() - x - 200,
                frame.getHeight() - y - 50, 100, 50, "Vertex");
        //The vertex button starts out as the selected button
        selectedButton = vertexButton;
        vertexButton.setButtonState(ButtonState.SELECTED);
        ModeButton connectionButton = new ModeButton(MouseMode.CONNECTION,
                frame.getWidth() - x - 100, frame.getHeight() - y - 50, 100, 50, "Connection");
        ModeButton removeButton = new ModeButton(MouseMode.REMOVE, frame.getWidth() - x - 300,
                frame.getHeight() - y - 50, 100, 50, "Remove");

        buttons.add(vertexButton);
        buttons.add(connectionButton);
        buttons.add(removeButton);

        ActionButton saveButton = new ActionButton(0, frame.getHeight() - y - 50, 100, 50,
                "Save", Action.SAVE);
        ActionButton loadButton = new ActionButton(100, frame.getHeight() - y - 50, 100, 50,
                "Load", Action.LOAD);
        buttons.add(saveButton);
        buttons.add(loadButton);
    }

    public void paintComponent(Graphics g) {
        g.setFont(drawFont);
        g.setColor(frame.getBackground());
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        g.setColor(Color.white);


        if (mode == MouseMode.CONNECTION && selectedVertex != null) {
            g.drawLine(selectedVertex.getX() + canvasX, selectedVertex.getY() + canvasY, mouseX,
                    mouseY);
        }

        graph.drawGraph(g, canvasX, canvasY);

        for (Button b : buttons) {
            b.draw(g);
        }

        if (graph != null && graph.getVertices() != null) {
            g.setColor(frame.getBackground());
            g.fillRect(0, 0, 200, 150);
            g.setColor(Color.white);
            g.drawString("Number of Vertices: " + graph.numberOfVertices(), 10, 20);
            //TODO For directed graphs don't divide by two
            g.drawString("Number of Connections: " + graph.numberOfConnections() / 2, 10, 40);
            g.drawString("Number of Colors: " + graph.numberOfColors(), 10, 60);
            g.drawString("Maximum Degree: " + graph.getPropertyFinder().maxDegree(graph), 10, 80);
            g.drawString("Bipartite: " + graph.getPropertyFinder().isBipartite(), 10, 100);
            g.drawString("Connected: " + isConnected, 10, 120);
            g.drawString("Tree: " + isTree, 10, 140);
        }

        info.draw(g);
    }

    private static void checkConnected() {
        isConnected = graph.getPropertyFinder().isConnected(graph);
        isTree = graph.getPropertyFinder().isTree(graph);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        mouseX = x;
        mouseY = y;

        if (draggingCanvas) {
            infoNode = null;
            canvasX = originalCanvasX - (originalMouseX - x);
            canvasY = originalCanvasY - (originalMouseY - y);
        }

        if (!dragged) {
            if (dragTimer == -1) {
                dragTimer = 3;
            } else {
                dragTimer--;
                if (dragTimer == 0) {
                    dragged = true;
                }
            }
        }

        if (mode == MouseMode.VERTEX) {
            if (selectedVertex != null) {
                info.setPosition(x, y);
                selectedVertex.setPosition(x - canvasX, y - canvasY);
            }
        }

        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        for (Button button : buttons) {
            if (button.getButtonState() != ButtonState.SELECTED && button.contains(x, y)) {
                button.setButtonState(ButtonState.HOVER);
            } else if (button.getButtonState() == ButtonState.HOVER) {
                button.setButtonState(ButtonState.NORMAL);
            }
        }

        // Highlight any vertices and connections that are within the removal range
        if (mode == MouseMode.REMOVE) {
            graph.highlightRemovals(x, y);
        }

        // Take into account the canvas movement when interacting with vertices
        int trueX = x - canvasX;
        int trueY = y - canvasY;

        infoNode = null;
        for (Vertex v : graph.getVertices()) {
            if (v.distance(trueX, trueY) < Vertex.getRadius()) {
                infoNode = v;
                info.setPosition(x, y);
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
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        // Take into account the canvas movement when interacting with vertices
        int trueX = x - canvasX;
        int trueY = y - canvasY;

        mouseX = x;
        mouseY = y;

        if (e.getButton() == MouseEvent.BUTTON1) {
            for (Button button : buttons) {
                if (button.contains(x, y)) {
                    //Only ModeButtons should be selected
                    if (button instanceof ModeButton) {
                        if (selectedButton != null) {
                            selectedButton.setButtonState(ButtonState.NORMAL);
                        }
                        button.setButtonState(ButtonState.SELECTED);
                        selectedButton = button;
                    }

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
                    if (vertex.distance(trueX, trueY) < Vertex.getRadius()) {
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.deselect();
                            selectedVertex = null;
                        }
                        selectedVertex = vertex;
                        vertex.select();
                        return;
                    }
                    if (vertex.isSelected()) {
                        vertex.deselect();
                    }
                }
                graph.createVertex(trueX, trueY);
                checkConnected();
            } else if (mode == MouseMode.CONNECTION) {
                for (Vertex v : graph.getVertices()) {
                    if (v.distance(trueX, trueY) < Vertex.getRadius()) {
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.deselect();
                            selectedVertex = null;
                        }
                        selectedVertex = v;
                        v.select();
                        return;
                    }
                    if (v.isSelected()) {
                        v.deselect();
                    }
                }
                if (selectedVertex != null)//There already is a selected node
                {
                    selectedVertex.deselect();
                    selectedVertex = null;
                }
            } else if (mode == MouseMode.REMOVE) {
                // Remove the vertices and connections that are within range
                graph.remove(trueX, trueY);
            }


        } else if (e.getButton() == MouseEvent.BUTTON3) {
            originalMouseX = x;
            originalMouseY = y;
            originalCanvasX = canvasX;
            originalCanvasY = canvasY;
            draggingCanvas = true;
        }

        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        mouseX = x;
        mouseY = y;

        // Take into account the canvas movement when interacting with vertices
        int trueX = x - canvasX;
        int trueY = y - canvasY;

        if (e.getButton() == MouseEvent.BUTTON1) {
            // If the mouse release came after dragging the mouse
            if (dragged) {
                if (mode == MouseMode.VERTEX) {
                    // Deselect the vertex that was dragged
                    if (selectedVertex != null) {
                        selectedVertex.deselect();
                    }
                    selectedVertex = null;
                } else if (mode == MouseMode.CONNECTION) {
                    // If we were dragging a connection from a vertex
                    if (selectedVertex != null) {
                        for (Vertex v : graph.getVertices()) {
                            // Skip the selected vertex as we don't want to have self-loops
                            if (v.equals(selectedVertex)) {
                                continue;
                            }
                            // If the cursor is within the vertex toggle a connection
                            if (v.distance(trueX, trueY) < Vertex.getRadius()) {
                                graph.toggleConnection(selectedVertex, v);
                                //TODO do not perform this for directed graphs
                                graph.toggleConnection(v, selectedVertex);
                                selectedVertex = null;
                                graph.removeHighlights();
                                repaint();
                                return;
                            }
                        }
                        // If there is no vertex to toggle a connection with deselect the vertex
                        if (selectedVertex != null) {
                            selectedVertex.deselect();
                            selectedVertex = null;
                        }
                    }
                }
            }
            dragged = false;
            dragTimer = -1;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            draggingCanvas = false;
        }

        repaint();
    }

    public static void reset() {
        graph = new Graph();
        infoNode = null;
        selectedVertex = null;
        isTree = true;
        isConnected = true;
    }
}