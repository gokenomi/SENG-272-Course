import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;

public class ProjectFormPanel extends JPanel {

        JTextField projectName = new JTextField(15);
        JTextField teamLeader = new JTextField(15);
        JTextField startDate = new JTextField(15);

        JComboBox<String> teamSize = new JComboBox<>(new String[]{"1-3","4-6","7-10","10+"});
        JComboBox<String> projectType = new JComboBox<>(new String[]{"Web","Mobile","Desktop","API"});

        JButton save = new JButton("Save");
        JButton clear = new JButton("Clear");

        public ProjectFormPanel(){

                setLayout(new GridLayout(6,2));

                add(new JLabel("Project Name"));
                add(projectName);

                add(new JLabel("Team Leader"));
                add(teamLeader);

                add(new JLabel("Team Size"));
                add(teamSize);

                add(new JLabel("Project Type"));
                add(projectType);

                add(new JLabel("Start Date"));
                add(startDate);

                add(save);
                add(clear);

                save.addActionListener(e -> saveProject());
                clear.addActionListener(e -> clearForm());
        }

        private void saveProject(){

                if(projectName.getText().isEmpty() || teamLeader.getText().isEmpty() || startDate.getText().isEmpty()){
                        JOptionPane.showMessageDialog(this,"Please fill all fields");
                        return;
                }

                try{
                        FileWriter fw = new FileWriter("projects.txt",true);
                        BufferedWriter bw = new BufferedWriter(fw);

                        bw.write("--- Project Entry ---\n");
                        bw.write("Project Name : " + projectName.getText() + "\n");
                        bw.write("Team Leader : " + teamLeader.getText() + "\n");
                        bw.write("Team Size : " + teamSize.getSelectedItem() + "\n");
                        bw.write("Project Type : " + projectType.getSelectedItem() + "\n");
                        bw.write("Start Date : " + startDate.getText() + "\n");
                        bw.write("Record Time : " + LocalDateTime.now() + "\n");
                        bw.write("---------------------\n");

                        bw.close();

                        JOptionPane.showMessageDialog(this,"Project Saved");

                }catch(Exception ex){
                        ex.printStackTrace();
                }
        }

        private void clearForm(){

                projectName.setText("");
                teamLeader.setText("");
                startDate.setText("");

                teamSize.setSelectedIndex(0);
                projectType.setSelectedIndex(0);
        }
}