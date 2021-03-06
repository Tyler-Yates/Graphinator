package util;

import graph.CanvasPosition;
import graph.Vertex;
import visualization.Drawer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

/**
 * Handles the saving and loading of graphs to disk.
 */
public class FileOperations {

    /**
     * Saves the current graph to disk.
     *
     * @throws IOException if there was an error creating the save file
     */
    public static void saveFile() throws IOException {
        final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        final int option = fileChooser.showOpenDialog(Drawer.getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.getSelectedFile() != null) {
                final File theFileToSave = fileChooser.getSelectedFile();
                final boolean fileDidNotExist = theFileToSave.createNewFile();
                if (fileDidNotExist) {
                    System.out.println("Creating new save file " + theFileToSave.getName());
                } else {
                    final int overwrite = JOptionPane.showConfirmDialog(null,
                            "File already exists. Overwrite?", "Overwrite File",
                            JOptionPane.YES_NO_OPTION);
                    switch (overwrite) {
                        case JOptionPane.YES_OPTION:
                            System.out.println("Overwriting file " + theFileToSave.getName());
                            break;
                        default:
                            System.out.println("Cancelling save");
                            return;
                    }
                }

                final PrintWriter printWriter = new PrintWriter(
                        new BufferedOutputStream(new FileOutputStream(theFileToSave)));
                printWriter.println("# Lines beginning with '#' are ignored");
                printWriter.println("# Number of Vertices:");
                final Collection<Vertex> vertices = Drawer.getGraph().getVertices();
                printWriter.println(vertices.size());
                printWriter.println("# Vertices");
                printWriter.println("# ID X Y");
                for (Vertex vertex : vertices) {
                    printWriter.println(vertex.getID() + " " + vertex.getX() + " " + vertex.getY());
                }
                printWriter.println("# Connections");
                printWriter.println("# ID : connectionID connectionID ...");
                for (Vertex vertex : vertices) {
                    // Don't print out a line for vertices that have no connections
                    if (vertex.getNeighbors().isEmpty()) {
                        continue;
                    }

                    printWriter.print(vertex.getID() + " ");
                    for (Vertex connected : vertex.getNeighbors()) {
                        printWriter.print(connected.getID() + " ");
                    }
                    printWriter.println();
                }

                printWriter.close();
            }
        }
    }

    /**
     * Loads a graph from disk.
     *
     * @throws IOException if there was an error loading the file
     * @throws MalformedGraphException if the graph in the file is invalid
     */
    public static void loadFile() throws IOException, MalformedGraphException {
        final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        final int option = fileChooser.showOpenDialog(Drawer.getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.getSelectedFile() != null) {
                final File fileToLoad = fileChooser.getSelectedFile();
                if (fileToLoad.exists()) {
                    Drawer.reset();

                    final Scanner input = new Scanner(fileToLoad);

                    // read in how many vertices there are in the graph
                    int numVertices = -1;
                    while (numVertices == -1) {
                        final String line = input.nextLine();
                        if (!line.startsWith("#")) {
                            numVertices = Integer.parseInt(line);
                        }
                    }

                    // create each vertex from its definition
                    while (Drawer.getGraph().getVertices().size() < numVertices && input.hasNext
                            ()) {
                        final String line = input.nextLine();
                        if (!line.startsWith("#")) {
                            final String[] sections = line.split(" ");
                            if (sections.length != 3) {
                                input.close();
                                throw new MalformedGraphException("Invalid vertex definition");
                            }

                            final int id = Integer.parseInt(sections[0]);
                            final int x = Integer.parseInt(sections[1]);
                            final int y = Integer.parseInt(sections[2]);
                            final CanvasPosition vertexPosition = new CanvasPosition(x, y);
                            Drawer.getGraph().createVertex(id, vertexPosition);
                        }
                    }

                    // ensure we have the correct number of vertex definitions
                    if (Drawer.getGraph().getVertices().size() < numVertices) {
                        input.close();
                        throw new MalformedGraphException("Missing vertex definition(s)");
                    }

                    // create the connections between vertices
                    while (input.hasNext()) {
                        final String line = input.nextLine();
                        if (!line.startsWith("#")) {
                            final String[] sections = line.split(" ");

                            final int id = Integer.parseInt(sections[0]);
                            final Vertex vertex = Drawer.getGraph().getVertex(id);
                            if (vertex == null) {
                                input.close();
                                throw new MalformedGraphException(
                                        "Invalid connection: vertex " + "does not exist");
                            }

                            for (int i = 1; i < sections.length; i++) {
                                final int otherID = Integer.parseInt(sections[i]);
                                final Vertex otherVertex = Drawer.getGraph().getVertex(otherID);
                                if (otherVertex == null) {
                                    input.close();
                                    throw new MalformedGraphException(
                                            "Invalid connection: " + "vertex does not exist");
                                }
                                Drawer.getGraph().addConnection(vertex, otherVertex);
                            }
                        }
                    }

                    input.close();
                }
            }
        }
    }
}