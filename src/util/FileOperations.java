package util;

import graph.Graph;
import graph.Vertex;
import visualization.Drawer;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileOperations {
    public static void saveFile() throws IOException {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        int option = fileChooser.showOpenDialog(Drawer.frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.getSelectedFile() != null) {
                File theFileToSave = fileChooser.getSelectedFile();
                theFileToSave.createNewFile();
                //Now write the vertices
                OutputStream file = new FileOutputStream(theFileToSave);
                OutputStream buffer = new BufferedOutputStream(file);
                ObjectOutput output = new ObjectOutputStream(buffer);
                output.writeObject(Drawer.graph.getVertices());
                output.close();
            }
        }
    }

    public static void loadFile() throws IOException, ClassNotFoundException {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        int option = fileChooser.showOpenDialog(Drawer.frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.getSelectedFile() != null) {
                File theFileToLoad = fileChooser.getSelectedFile();
                if (theFileToLoad.exists()) {
                    Drawer.reset();
                    InputStream file = new FileInputStream(theFileToLoad);
                    InputStream buffer = new BufferedInputStream(file);
                    ObjectInput input = new ObjectInputStream(buffer);
                    Drawer.graph = new Graph((ArrayList<Vertex>) input.readObject());
                    input.close();
                    Drawer.initialize();
                    Drawer.initLines();
                }
            }
        }
    }
}