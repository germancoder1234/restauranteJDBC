import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;

public class App {

	private JFrame frame;
	private JTextField textFieldIdIngrediente;
	private JTextField textFieldNombreIngrediente;
	private JTextField textFieldCalorias;
	private JTextField textFieldIdReceta;
	private JTextField textFieldNombreReceta;
	private JTextField textFieldTiempo;
	private JTextField textFieldGramos;

	DefaultTableModel model;
	DefaultTableModel modelo;
	DefaultTableModel modelom;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public App() {
		initialize();
	}

	// ===================== CARGAS =====================

	private void cargarIngredientes() {
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ingrediente");

			model.setRowCount(0);

			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("id");
				row[1] = rs.getInt("calorias");
				row[2] = rs.getString("nombre");
				model.addRow(row);
			}

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarRecetas() {
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM receta");

			modelo.setRowCount(0);

			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("id");
				row[1] = rs.getString("nombre");
				row[2] = rs.getInt("tiempo");
				modelo.addRow(row);
			}

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarComida() {
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM comida");

			modelom.setRowCount(0);

			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("id_ingrediente");
				row[1] = rs.getInt("id_receta");
				row[2] = rs.getInt("gramos");
				modelom.addRow(row);
			}

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// ===================== INIT =====================

	private void initialize() {
		
		

		frame = new JFrame();
		frame.setBounds(100, 100, 770, 443);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// ================= INGREDIENTE =================

		textFieldIdIngrediente = new JTextField();
		textFieldIdIngrediente.setEditable(false);
		textFieldIdIngrediente.setBounds(22, 195, 114, 21);
		frame.getContentPane().add(textFieldIdIngrediente);

		textFieldCalorias = new JTextField();
		textFieldCalorias.setBounds(22, 240, 114, 21);
		frame.getContentPane().add(textFieldCalorias);

		textFieldNombreIngrediente = new JTextField();
		textFieldNombreIngrediente.setBounds(22, 287, 114, 21);
		frame.getContentPane().add(textFieldNombreIngrediente);

		model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("Calorias");
		model.addColumn("Nombre");

		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(23, 12, 230, 156);
		frame.getContentPane().add(scrollPane);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				textFieldIdIngrediente.setText(model.getValueAt(i, 0).toString());
				textFieldCalorias.setText(model.getValueAt(i, 1).toString());
				textFieldNombreIngrediente.setText(model.getValueAt(i, 2).toString());
			}
		});

		JButton btnGuardarIng = new JButton("Guardar");
		btnGuardarIng.setBounds(153, 192, 100, 27);
		frame.getContentPane().add(btnGuardarIng);

		btnGuardarIng.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"INSERT INTO ingrediente (calorias, nombre) VALUES (?, ?)"
					);

					ps.setInt(1, Integer.parseInt(textFieldCalorias.getText()));
					ps.setString(2, textFieldNombreIngrediente.getText());

					ps.executeUpdate();
					ps.close();

					cargarIngredientes();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		JButton btnActualizarIng = new JButton("Actualizar");
		btnActualizarIng.setBounds(153, 237, 100, 27);
		frame.getContentPane().add(btnActualizarIng);

		btnActualizarIng.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"UPDATE ingrediente SET calorias=?, nombre=? WHERE id=?"
					);

					ps.setInt(1, Integer.parseInt(textFieldCalorias.getText()));
					ps.setString(2, textFieldNombreIngrediente.getText());
					ps.setInt(3, Integer.parseInt(textFieldIdIngrediente.getText()));

					ps.executeUpdate();
					ps.close();

					cargarIngredientes();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		JButton btnBorrarIng = new JButton("Borrar");
		btnBorrarIng.setBounds(148, 284, 105, 27);
		frame.getContentPane().add(btnBorrarIng);

		btnBorrarIng.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"DELETE FROM ingrediente WHERE id=?"
					);

					ps.setInt(1, Integer.parseInt(textFieldIdIngrediente.getText()));

					ps.executeUpdate();
					ps.close();

					cargarIngredientes();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		// ================= RECETAS =================

		textFieldIdReceta = new JTextField();
		textFieldIdReceta.setEnabled(false);
		textFieldIdReceta.setBounds(488, 195, 114, 21);
		frame.getContentPane().add(textFieldIdReceta);

		textFieldNombreReceta = new JTextField();
		textFieldNombreReceta.setBounds(488, 240, 114, 21);
		frame.getContentPane().add(textFieldNombreReceta);

		textFieldTiempo = new JTextField();
		textFieldTiempo.setBounds(488, 287, 114, 21);
		frame.getContentPane().add(textFieldTiempo);

		modelo = new DefaultTableModel();
		modelo.addColumn("ID");
		modelo.addColumn("Nombre");
		modelo.addColumn("Tiempo");

		JTable tableReceta = new JTable(modelo);
		JScrollPane scrollPane2 = new JScrollPane(tableReceta);
		scrollPane2.setBounds(490, 12, 250, 155);
		frame.getContentPane().add(scrollPane2);
		
		JLabel lblIngredientes = new JLabel("");
		lblIngredientes.setBounds(500, 342, 214, 14);
		frame.getContentPane().add(lblIngredientes);

		tableReceta.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int i = tableReceta.getSelectedRow();
				textFieldIdReceta.setText(modelo.getValueAt(i, 0).toString());
				textFieldNombreReceta.setText(modelo.getValueAt(i, 1).toString());
				textFieldTiempo.setText(modelo.getValueAt(i, 2).toString());
				
				try {
			    	Connection	con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM comida WHERE id_receta = ?");
								
					ps.setInt(1, Integer.parseInt(textFieldIdReceta.getText()));
					
			ResultSet rs=ps.executeQuery();
			if (rs.next()) {
				int total = rs.getInt(1);
				lblIngredientes.setText("Ingredientes: " + total);
			}

			rs.close();
			ps.close();
					

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}		
				
			}
		});

		JButton btnGuardarRec = new JButton("Guardar");
		btnGuardarRec.setBounds(628, 192, 105, 27);
		frame.getContentPane().add(btnGuardarRec);

		btnGuardarRec.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"INSERT INTO receta (nombre, tiempo) VALUES (?, ?)"
					);

					ps.setString(1, textFieldNombreReceta.getText());
					ps.setInt(2, Integer.parseInt(textFieldTiempo.getText()));

					ps.executeUpdate();
					ps.close();

					cargarRecetas();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		JButton btnActualizarRec = new JButton("Actualizar");
		btnActualizarRec.setBounds(628, 237, 105, 27);
		frame.getContentPane().add(btnActualizarRec);

		btnActualizarRec.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"UPDATE receta SET nombre=?, tiempo=? WHERE id=?"
					);

					ps.setString(1, textFieldNombreReceta.getText());
					ps.setInt(2, Integer.parseInt(textFieldTiempo.getText()));
					ps.setInt(3, Integer.parseInt(textFieldIdReceta.getText()));

					ps.executeUpdate();
					ps.close();

					cargarRecetas();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		JButton btnBorrarRec = new JButton("Borrar");
		btnBorrarRec.setBounds(628, 284, 105, 27);
		frame.getContentPane().add(btnBorrarRec);

		btnBorrarRec.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"DELETE FROM receta WHERE id=?"
					);

					ps.setInt(1, Integer.parseInt(textFieldIdReceta.getText()));

					ps.executeUpdate();
					ps.close();

					cargarRecetas();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		// ================= COMIDA =================

		textFieldGramos = new JTextField();
		textFieldGramos.setBounds(312, 339, 114, 21);
		frame.getContentPane().add(textFieldGramos);

		modelom = new DefaultTableModel();
		modelom.addColumn("id ingrediente");
		modelom.addColumn("id receta");
		modelom.addColumn("gramos");

		JTable tablaMatriz = new JTable(modelom);
		JScrollPane scrollPane3 = new JScrollPane(tablaMatriz);
		scrollPane3.setBounds(263, 12, 213, 156);
		frame.getContentPane().add(scrollPane3);

		
		tablaMatriz.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				textFieldIdIngrediente.setText(modelom.getValueAt(tablaMatriz.getSelectedRow(), 0).toString());
				textFieldIdReceta.setText(modelom.getValueAt(tablaMatriz.getSelectedRow(), 1).toString());
				textFieldGramos.setText(modelom.getValueAt(tablaMatriz.getSelectedRow(), 2).toString());

				
				
				
			}
		});

		JButton btnGuardarCom = new JButton("Guardar");
		btnGuardarCom.setBounds(321, 192, 105, 27);
		frame.getContentPane().add(btnGuardarCom);

		btnGuardarCom.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"INSERT INTO comida VALUES (?, ?, ?)"
					);

					ps.setInt(1, Integer.parseInt(textFieldIdIngrediente.getText()));
					ps.setInt(2, Integer.parseInt(textFieldIdReceta.getText()));
					ps.setInt(3, Integer.parseInt(textFieldGramos.getText()));

					ps.executeUpdate();
					ps.close();

					cargarComida();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		JButton btnActualizarCom = new JButton("Actualizar");
		btnActualizarCom.setBounds(321, 237, 105, 27);
		frame.getContentPane().add(btnActualizarCom);

		btnActualizarCom.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"UPDATE comida SET gramos=? WHERE id_ingrediente=? AND id_receta=?"
					);

					ps.setInt(1, Integer.parseInt(textFieldGramos.getText()));
					ps.setInt(2, Integer.parseInt(textFieldIdIngrediente.getText()));
					ps.setInt(3, Integer.parseInt(textFieldIdReceta.getText()));

					ps.executeUpdate();
					ps.close();

					cargarComida();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});

		JButton btnBorrarCom = new JButton("Borrar");
		btnBorrarCom.setBounds(321, 284, 105, 27);
		frame.getContentPane().add(btnBorrarCom);
		
		
		
		JLabel lblNewLabel = new JLabel("gramos");
		lblNewLabel.setBounds(318, 326, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("id");
		lblNewLabel_1.setBounds(33, 179, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("nombre");
		lblNewLabel_2.setBounds(32, 272, 46, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("calorias");
		lblNewLabel_3.setBounds(32, 227, 68, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("id");
		lblNewLabel_4.setBounds(500, 178, 46, 14);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("nombre");
		lblNewLabel_5.setBounds(498, 227, 46, 14);
		frame.getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("tiempo");
		lblNewLabel_6.setBounds(498, 272, 74, 14);
		frame.getContentPane().add(lblNewLabel_6);
		
		JButton btnCalorias = new JButton("Más calórico");
		btnCalorias.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();

					PreparedStatement ps = con.prepareStatement(
						"SELECT MAX(calorias) FROM ingrediente"
					);

					ResultSet rs = ps.executeQuery();

					if (rs.next()) {
						int calorias = rs.getInt(1);

						PreparedStatement ps2 = con.prepareStatement(
							"SELECT nombre FROM ingrediente WHERE calorias = ?"
						);

						ps2.setInt(1, calorias);

						ResultSet rs2 = ps2.executeQuery();

						if (rs2.next()) {
							String nombre = rs2.getString("nombre");

							JOptionPane.showMessageDialog(frame,
								"El ingrediente con más calorías es " + nombre
							);
						}

						rs2.close();
						ps2.close();
					}

					rs.close();
					ps.close();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCalorias.setBounds(47, 352, 131, 23);
		frame.getContentPane().add(btnCalorias);

		btnBorrarCom.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement(
						"DELETE FROM comida WHERE id_ingrediente=? AND id_receta=?"
					);

					ps.setInt(1, Integer.parseInt(textFieldIdIngrediente.getText()));
					ps.setInt(2, Integer.parseInt(textFieldIdReceta.getText()));

					ps.executeUpdate();
					ps.close();

					cargarComida();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		cargarIngredientes();
		cargarRecetas();
		cargarComida();

		
	}
}