package com.adera.gui;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Volume;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Timer;

public class Monitor extends JFrame {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JLabel discUse;
    private JLabel netUse;

    public Monitor() {
        setTitle("Monitoramento");
        setBounds(0, 0, 500, 300);
        setResizable(false);
        setContentPane(contentPane);

        var looca = new Looca();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                var discData = looca.getGrupoDeDiscos().getVolumes();
                Long total = 0L;
                Long available = 0L;
                for(Volume data: discData){
                    total += data.getTotal();
                    available += data.getDisponivel();
                }
                Long inUse = total - available;
                Double percentageUsing = (inUse.doubleValue()/total)*100;
                discUse.setText(String.format("%.0f%%", percentageUsing));

                try {
                    InetAddress address = InetAddress.getByName("www.google.com");
                    long startTime = System.currentTimeMillis();
                    if (address.isReachable(1000)) {
                        long endTime = System.currentTimeMillis();
                        long latency = endTime - startTime;
                        netUse.setText(String.format("%d ms", latency));
                    } else {
                        System.out.println("O endereço IP não está acessível.");
                    }
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    System.out.println("Ocorreu um erro ao calcular a latência da rede: " + e.getMessage());
                }

            }
        };

        java.util.Timer timer = new Timer();

        timer.schedule(task, new java.util.Date(), 1000);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Monitor dialog = new Monitor();
        dialog.setVisible(true);
    }
}
