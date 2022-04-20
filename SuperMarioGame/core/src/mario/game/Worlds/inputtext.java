package mario.game.Worlds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import mario.game.Main;
import mario.game.Objects.Mario;

public class inputtext extends JFrame {

    JTextArea textarea;
    JButton button;
    JLabel label;

    public String st;
    private Main zaidimas;
    private Mario zaidejas;
    String text="";


    public inputtext(){

        setLayout(new FlowLayout());

        textarea = new JTextArea(5,30);
        add(textarea);

        button = new JButton("Click");
        add(button);

        label = new JLabel("");
        add(label);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                st = textarea.getText();
                label.setText(st);

                //MainCodeProgram g = new MainCodeProgram();
                //input(st);
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,200);
        setVisible(true);

    }

    public String taip(){
        return st;
    }
}
