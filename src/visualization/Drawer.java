package visualization;

import graph.Edge;
import graph.Graph;
import graph.PropertyFinder;
import graph.Vertex;
import util.Action;
import util.MouseMode;

import javax.swing.*;
import java.awt.*;
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

    static int oldWidth, oldHeight;

    static long dragTimer = -1;

    static int canvasX, canvasY;

    static int mouseX, mouseY;

    static boolean draggingCanvas = false;
    static int originalCanvasX, originalCanvasY;
    static int originalMouseX, originalMouseY;

    private static final double REMOVAL_DISTANCE = 7.5;

    public static MouseMode mode = MouseMode.VERTEX;
    private static Button selectedButton;

    static ArrayList<Button> buttons = new ArrayList<Button>();

    static ArrayList<Edge> lines = new ArrayList<Edge>();

    static boolean dragged = false;

    static Font drawFont = null;

    static boolean isConnected = true;
    static boolean isTree = true;

    public Drawer() {
        frame = new JFrame("Graphinator v" + VERSION);
        frame.setVisible(true);
        oldWidth = 1024;
        oldHeight = 720;
        frame.setSize(oldWidth, oldHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.addMouseMotionListener(this);
        frame.addMouseListener(this);
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
        if (frame.getWidth() != oldWidth || frame.getHeight() != oldHeight) {
            oldWidth = frame.getWidth();
            oldHeight = frame.getHeight();
            initButtons();
        }
        g.setColor(frame.getBackground());
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        g.setColor(Color.white);


        if (mode == MouseMode.CONNECTION && selectedVertex != null) {
            g.drawLine(selectedVertex.getX() + canvasX, selectedVertex.getY() + canvasY, mouseX,
                    mouseY);
        }

        for (Edge edge : lines) {
            edge.paint(g);
        }

        /*if (selectedVertex != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2.5f));
            selectedVertex.drawConnections(g, canvasX, canvasY);
            g2.setStroke(new BasicStroke(1f));
        }*/
        for (Vertex v : graph.getVertices()) {
            v.draw(g, canvasX, canvasY);
        }

        for (Button b : buttons) {
            b.draw(g);
        }

        if (graph != null && graph.getVertices() != null) {
            g.setColor(frame.getBackground());
            g.fillRect(0, 0, 200, 150);
            g.setColor(Color.white);
            g.drawString("Number of Vertices: " + graph.vertexCount(), 10, 20);
            g.drawString("Number of Connections: " + graph.connectionCount() / 2, 10, 40);
            g.drawString("Number of Colors: " + Vertex.getMaxColor(), 10, 60);
            g.drawString("Maximum Degree: " + PropertyFinder.maxDegree(graph), 10, 80);
            g.drawString("Bipartite: " + PropertyFinder.isBipartite(), 10, 100);
            g.drawString("Connected: " + isConnected, 10, 120);
            g.drawString("Tree: " + isTree, 10, 140);
        }

        info.draw(g);
    }

    private static void resetColor() {
        Vertex.resetMaxColor();
        for (Vertex v : graph.getVertices()) {
            v.resetColor();
        }
    }

    private static void checkConnected() {
        isConnected = PropertyFinder.isConnected(graph);
        isTree = PropertyFinder.isTree(graph);
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

    public static void initLines() {
        lines.clear();
        for (Vertex v : graph.getVertices()) {
            for (Vertex vv : v.getNeighbors()) {
                Edge temp = new Edge(v, vv);
                if (!lines.contains(temp)) {
                    lines.add(temp);
                }
            }
        }
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

        // highlight any edges that are within the removal range
        if (mode == MouseMode.REMOVE) {
            for (Edge edge : lines) {
                if (edge.distance(x, y) < REMOVAL_DISTANCE) {
                    edge.select();
                } else {
                    edge.deselect();
                }
            }
        }

        for (int i = 0; i < graph.vertexCount(); i++) {
            Vertex v = graph.getVertex(i);
            double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                    2) + Math.pow(y - canvasY - v.getY(), 2));
            if (distance < Vertex.getRadius()) {
                infoNode = v;
                info.setPosition(x, y);
                return;
            }
        }
        infoNode = null;

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
                }
            }

            if (mode == MouseMode.VERTEX) {
                for (int i = 0; i < graph.vertexCount(); i++) {
                    Vertex v = graph.getVertex(i);
                    double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                            2) + Math.pow(y - canvasY - v.getY(), 2));
                    if (distance < Vertex.getRadius()) {
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.unselect();
                            selectedVertex = null;
                        }
                        selectedVertex = v;
                        v.select();
                        return;
                    }
                    if (v.isSelected()) {
                        v.unselect();
                    }
                }
                Vertex temp = new Vertex(x - canvasX, y - canvasY);
                temp.initialize();
                graph.addVertex(temp);
                checkConnected();
            } else if (mode == MouseMode.CONNECTION) {
                for (int i = 0; i < graph.vertexCount(); i++) {
                    Vertex v = graph.getVertex(i);
                    double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                            2) + Math.pow(y - canvasY - v.getY(), 2));
                    if (distance < Vertex.getRadius()) {
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.unselect();
                            selectedVertex = null;
                        }
                        selectedVertex = v;
                        v.select();
                        return;
                    }
                    if (v.isSelected()) {
                        v.unselect();
                    }
                }
                if (selectedVertex != null)//There already is a selected node
                {
                    selectedVertex.unselect();
                    selectedVertex = null;
                }
            } else if (mode == MouseMode.REMOVE) {
                for (int i = 0; i < graph.vertexCount(); i++) {
                    Vertex v = graph.getVertex(i);
                    double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                            2) + Math.pow(y - canvasY - v.getY(), 2));
                    if (distance < Vertex.getRadius()) {
                        for (Vertex temp : v.getNeighbors()) {
                            temp.removeConnection(v);
                        }
                        graph.removeVertex(v);
                        resetColor();
                        setColor();
                        return;
                    }
                }

                for (int i=0; i<lines.size(); i++) {
                    final Edge edge = lines.get(i);

                    double dist = edge.distance(x - canvasX, y - canvasY);
                    if (dist < REMOVAL_DISTANCE) {
                        edge.getStart().removeConnection(edge);
                        edge.getEnd().removeConnection(edge);
                        lines.remove(i);
                        resetColor();
                        setColor();
                        checkConnected();
                        i--;
                    }
                }
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

    private static void setColor() {
        if (graph.vertexCount() == 0) {
            return;
        }
        graph.getVertex(0).initialize();
        for (int i = 1; i < graph.vertexCount(); i++) {
            Vertex v = graph.getVertex(i);
            v.chooseColor();
        }
        Vertex.initColors();
        checkConnected();
    }

    public void mouseReleased(MouseEvent e) {
        int x = e.getX() - frame.getInsets().left;
        int y = e.getY() - frame.getInsets().top;

        mouseX = x;
        mouseY = y;

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (dragged) {
                if (mode == MouseMode.VERTEX) {
                    if (selectedVertex != null) {
                        selectedVertex.unselect();
                    }
                    selectedVertex = null;
                } else if (mode == MouseMode.CONNECTION) {
                    if (selectedVertex != null) {
                        for (int i = 0; i < graph.vertexCount(); i++) {
                            Vertex v = graph.getVertex(i);
                            if (v.equals(selectedVertex)) {
                                continue;
                            }
                            double distance = Math.sqrt(Math.pow(x - canvasX - v.getX(),
                                    2) + Math.pow(y - canvasY - v.getY(), 2));
                            if (distance < Vertex.getRadius()) {
                                if (v.isNeighbor(selectedVertex)) {
                                    v.removeConnection(selectedVertex);
                                    selectedVertex.removeConnection(v);
                                    resetColor();
                                    selectedVertex.unselect();
                                    selectedVertex = null;
                                    setColor();

                                    repaint();

                                    return;
                                } else {
                                    resetColor();
                                    lines.add(new Edge(v, selectedVertex));
                                    addConnection(selectedVertex, v);
                                    addConnection(v, selectedVertex);
                                    selectedVertex.unselect();
                                    info.setPosition(x, y);
                                    infoNode = v;
                                    selectedVertex = null;
                                    setColor();

                                    repaint();

                                    return;
                                }
                            }
                        }
                        if (selectedVertex != null)//There already is a selected node
                        {
                            selectedVertex.unselect();
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

    public static void initialize() {
        resetColor();
        setColor();
        checkConnected();
    }

    public static void addConnection(Vertex start, Vertex end) {
        final Edge edge = new Edge(start, end);
        lines.add(edge);
        start.addConnection(edge);
    }
}