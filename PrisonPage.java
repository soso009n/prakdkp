package ui;

import model.Narapidana;
import util.DummyData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class PrisonPage extends JPanel {
    private JPanel prisonerPanel;
    private JButton backButton;
    private JButton logoutButton;
    private JComboBox<String> prisonTypeComboBox;
    private JComboBox<String> categoryComboBox;
    private JTextField searchField;

    public PrisonPage(ActionListener backListener, ActionListener logoutListener) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 69, 0)); 
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(logoutListener);
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        prisonTypeComboBox = new JComboBox<>(new String[]{"Ringan", "Sedang", "Berat"});
        comboBoxPanel.add(prisonTypeComboBox);

        categoryComboBox = new JComboBox<>(new String[]{"Laki-Laki", "Perempuan", "Anak-Anak"});
        comboBoxPanel.add(categoryComboBox);

        searchField = new JTextField(20);
        searchField.setToolTipText("Search prisoners by name or alias");
        comboBoxPanel.add(searchField);

        JButton searchButton = new JButton("Cari");
        searchButton.setBackground(new Color(255, 215, 0)); 
        searchButton.setForeground(Color.BLACK);
        comboBoxPanel.add(searchButton);

        centerPanel.add(comboBoxPanel, BorderLayout.NORTH);

        prisonerPanel = new JPanel();
        prisonerPanel.setLayout(new BoxLayout(prisonerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(prisonerPanel);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back");
        backButton.setBackground(new Color(0, 128, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(backListener);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);


        prisonTypeComboBox.addActionListener(e -> updatePrisoners());
        categoryComboBox.addActionListener(e -> updatePrisoners());
        searchButton.addActionListener(e -> updatePrisoners());
        searchField.addActionListener(e -> updatePrisoners());

        updatePrisoners();
    }

    private void updatePrisoners() {
        prisonerPanel.removeAll();

        String selectedType = (String) prisonTypeComboBox.getSelectedItem();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        String searchQuery = searchField.getText().toLowerCase();

        List<Narapidana> prisoners = DummyData.getPrisoners(selectedCategory, selectedType);

        if (prisoners.isEmpty()) {
            JLabel emptyLabel = new JLabel("No prisoners found for the selected type.");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            prisonerPanel.add(emptyLabel);
        } else {
            for (Narapidana prisoner : prisoners) {
                boolean isAdult = selectedCategory.equals("Laki-Laki") || selectedCategory.equals("Perempuan");
                boolean isMale = selectedCategory.equals("Laki-Laki");

                if ((isAdult && prisoner.isAdult() && prisoner.isMale() == isMale) ||
                        (!isAdult && !prisoner.isAdult() && prisoner.isMale() == isMale)) {
                    String name = prisoner.getNamaLengkap().toLowerCase();
                    String alias = prisoner.getNamaSamaran().toLowerCase();
                    if (name.contains(searchQuery) || alias.contains(searchQuery)) {
                        prisonerPanel.add(createPrisonerInfoPanel(prisoner));
                    }
                }
            }
        }

        prisonerPanel.revalidate();
        prisonerPanel.repaint();
    }

    private JPanel createPrisonerInfoPanel(Narapidana prisoner) {
        JPanel prisonerInfoPanel = new JPanel(new BorderLayout());
        prisonerInfoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        prisonerInfoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(prisoner.getNamaLengkap() + " (" + prisoner.getNamaSamaran() + ")");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTextArea detailsArea = new JTextArea(
                "Umur: " + prisoner.getUmur() + "\n" +
                        "Sisa Masa Tahanan: " + prisoner.getSisaMasaTahanan() + " bulan\n" +
                        "Pelanggaran: " + prisoner.getPelanggaran() + "\n" +
                        "Tanggal Mulai: " + prisoner.getTanggalMulai() + "\n" +
                        "Tanggal Keluar: " + prisoner.getTanggalKeluar()
        );
        detailsArea.setEditable(false);
        detailsArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        detailsArea.setBackground(Color.LIGHT_GRAY);

        prisonerInfoPanel.add(nameLabel, BorderLayout.NORTH);
        prisonerInfoPanel.add(detailsArea, BorderLayout.CENTER);

        return prisonerInfoPanel;
    }
}
