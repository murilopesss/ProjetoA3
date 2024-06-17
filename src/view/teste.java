package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class teste {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Minha Aplicação GUI com Botão");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JButton button = new JButton("Clique-me");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Botão clicado!");
            }
        });

        frame.getContentPane().add(button);
        frame.setVisible(true);
    }
}
