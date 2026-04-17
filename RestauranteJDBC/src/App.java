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

public class App {

	private JFrame frame;
	private JTextField textFieldIdIngrediente;
	private JTextField textFieldNombreIngrediente;
	private JTextField textFieldCalorias;
	private JTextField textFieldIdReceta;
	private JTextField textFieldNombreReceta;
	private JTextField textFieldTiempo;
	private JTextField textFieldGramos;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 770, 443);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textFieldIdIngrediente = new JTextField();
		textFieldIdIngrediente.setEditable(false);
		textFieldIdIngrediente.setBounds(22, 195, 114, 21);
		frame.getContentPane().add(textFieldIdIngrediente);
		textFieldIdIngrediente.setColumns(10);
		
		textFieldNombreIngrediente = new JTextField();
		textFieldNombreIngrediente.setBounds(22, 287, 114, 21);
		frame.getContentPane().add(textFieldNombreIngrediente);
		textFieldNombreIngrediente.setColumns(10);
		
		textFieldCalorias = new JTextField();
		textFieldCalorias.setBounds(27, 240, 114, 21);
		frame.getContentPane().add(textFieldCalorias);
		textFieldCalorias.setColumns(10);
		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("calorias");
		model.addColumn("nombre");
		
		
		
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
					

		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int index = table.getSelectedRow();
				TableModel model = table.getModel();
			
				textFieldIdIngrediente.setText(model.getValueAt(index, 0).toString());
				textFieldCalorias.setText(model.getValueAt(index, 1).toString());
				textFieldNombreIngrediente.setText(model.getValueAt(index, 2).toString());
				
				
				
			}
		});
		scrollPane.setBounds(23, 12, 230, 156);
		frame.getContentPane().add(scrollPane);
		
		JButton btnGuardarIngrediente = new JButton("Guardar");
		btnGuardarIngrediente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement("INSERT INTO ingrediente (calorias, nombre) VALUES (?, ?)");
					
					ps.setInt(1, Integer.parseInt(textFieldCalorias.getText()));
					ps.setString(2, textFieldNombreIngrediente.getText());

					ps.executeUpdate();
					ps.close();
					
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
		});
		btnGuardarIngrediente.setBounds(153, 192, 100, 27);
		frame.getContentPane().add(btnGuardarIngrediente);
		
		JButton btnActualizarIngrediente = new JButton("Actualizar");
		btnActualizarIngrediente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {    
				Connection con = ConnectionSingleton.getConnection();
				PreparedStatement ps = con.prepareStatement("UPDATE ingrediente SET calorias = ? , nombre = ? WHERE id = ?");
				
				
				ps.setInt(1, Integer.parseInt(textFieldCalorias.getText()));
				ps.setString(2, textFieldNombreIngrediente.getText());
				ps.setInt(3, Integer.parseInt(textFieldIdIngrediente.getText()));
				
				ps.executeUpdate();
				ps.close();
				
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
		});
		btnActualizarIngrediente.setBounds(153, 237, 100, 27);
		frame.getContentPane().add(btnActualizarIngrediente);
		
		JButton btnBorrarIngredient = new JButton("Borrar");
		btnBorrarIngredient.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement("DELETE FROM ingrediente WHERE id = ?");
					
					ps.setInt(1, Integer.parseInt(textFieldIdIngrediente.getText()));

					ps.executeUpdate();
					ps.close();
					
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
		});
		btnBorrarIngredient.setBounds(148, 284, 105, 27);
		frame.getContentPane().add(btnBorrarIngredient);
		
		JLabel lblId = new JLabel("id");
		lblId.setBounds(51, 176, 60, 17);
		frame.getContentPane().add(lblId);
		
		JLabel lblCalorias = new JLabel("calorias");
		lblCalorias.setBounds(51, 226, 60, 17);
		frame.getContentPane().add(lblCalorias);
		
		JLabel lblNombre = new JLabel("nombre");
		lblNombre.setBounds(51, 270, 60, 17);
		frame.getContentPane().add(lblNombre);
		
		JLabel lblId_1 = new JLabel("id");
		lblId_1.setBounds(501, 176, 60, 17);
		frame.getContentPane().add(lblId_1);
		
		JLabel lblNombre_1 = new JLabel("nombre");
		lblNombre_1.setBounds(501, 226, 60, 17);
		frame.getContentPane().add(lblNombre_1);
		
		JLabel lblTiempo = new JLabel("tiempo");
		lblTiempo.setBounds(501, 270, 60, 17);
		frame.getContentPane().add(lblTiempo);
		
		textFieldIdReceta = new JTextField();
		textFieldIdReceta.setEnabled(false);
		textFieldIdReceta.setBounds(488, 195, 114, 21);
		frame.getContentPane().add(textFieldIdReceta);
		textFieldIdReceta.setColumns(10);
		
		textFieldNombreReceta = new JTextField();
		textFieldNombreReceta.setBounds(488, 240, 114, 21);
		frame.getContentPane().add(textFieldNombreReceta);
		textFieldNombreReceta.setColumns(10);
		
		textFieldTiempo = new JTextField();
		textFieldTiempo.setBounds(488, 287, 114, 21);
		frame.getContentPane().add(textFieldTiempo);
		textFieldTiempo.setColumns(10);
		
		DefaultTableModel modelo = new DefaultTableModel();
		modelo.addColumn("ID");
		modelo.addColumn("Nombre");
		modelo.addColumn("Minutos");

		try {
		    Connection con = ConnectionSingleton.getConnection();
		    Statement stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT * FROM receta");

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

		JTable tablea = new JTable(modelo);
		JScrollPane scrollPane2 = new JScrollPane(tablea);
		tablea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = tablea.getSelectedRow();
				TableModel modelo = tablea.getModel();
			
				textFieldIdReceta.setText(modelo.getValueAt(index, 0).toString());
				textFieldNombreReceta.setText(modelo.getValueAt(index, 1).toString());
				textFieldTiempo.setText(modelo.getValueAt(index, 2).toString());
			}
		});
	
		scrollPane2.setBounds(490, 12, 250, 155);
		frame.getContentPane().add(scrollPane2);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement("INSERT INTO receta (nombre, tiempo) VALUES (?, ?)");
					
					ps.setString(1, textFieldNombreReceta.getText());
					ps.setInt(2, Integer.parseInt(textFieldTiempo.getText()));

					ps.executeUpdate();
					ps.close();
					
					Statement stmt = con.createStatement();
				    ResultSet rs = stmt.executeQuery("SELECT * FROM receta");
				    modelo.setRowCount(0);
				    while (rs.next()) {
				        Object[] rowa = new Object[3];
				        rowa[0] = rs.getInt("id");
				        rowa[1] = rs.getString("nombre");
				        rowa[2] = rs.getInt("tiempo"); 

				        modelo.addRow(rowa);
				    }

				    rs.close();
				    con.close();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		btnGuardar.setBounds(628, 192, 105, 27);
		frame.getContentPane().add(btnGuardar);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement("UPDATE receta SET nombre = ?, tiempo = ? WHERE id = ?");

						ps.setString(1, textFieldNombreReceta.getText());
						ps.setInt(2, Integer.parseInt(textFieldTiempo.getText()));
						ps.setInt(3, Integer.parseInt(textFieldIdReceta.getText()));
					
					ps.executeUpdate();
					ps.close();
					
					Statement stmt = con.createStatement();
				    ResultSet rs = stmt.executeQuery("SELECT * FROM receta");
				    modelo.setRowCount(0);
				    while (rs.next()) {
				        Object[] rowa = new Object[3];
				        rowa[0] = rs.getInt("id");
				        rowa[1] = rs.getString("nombre");
				        rowa[2] = rs.getInt("tiempo"); 

				        modelo.addRow(rowa);
				    }

				    rs.close();
				    con.close();
				
				
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		
			
		});
		btnActualizar.setBounds(628, 237, 105, 27);
		frame.getContentPane().add(btnActualizar);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement("DELETE FROM receta WHERE id = ?");
					
					ps.setString(1, textFieldIdReceta.getText());

					ps.executeUpdate();
					ps.close();
					
					Statement stmt = con.createStatement();
				    ResultSet rs = stmt.executeQuery("SELECT * FROM receta");
				    modelo.setRowCount(0);
				    while (rs.next()) {
				        Object[] rowa = new Object[3];
				        rowa[0] = rs.getInt("id");
				        rowa[1] = rs.getString("nombre");
				        rowa[2] = rs.getInt("tiempo"); 

				        modelo.addRow(rowa);
				    }

				    rs.close();
				    con.close();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBorrar.setBounds(628, 284, 105, 27);
		frame.getContentPane().add(btnBorrar);
		
		textFieldGramos = new JTextField();
		textFieldGramos.setBounds(312, 339, 114, 21);
		frame.getContentPane().add(textFieldGramos);
		textFieldGramos.setColumns(10);
		
		
		DefaultTableModel modelom = new DefaultTableModel();
		modelom.addColumn("id ingrediente");
		modelom.addColumn("id receta");
		modelom.addColumn("gramos");

		try {
		    Connection con = ConnectionSingleton.getConnection();
		    Statement stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT * FROM comida");

		    while (rs.next()) {
		        Object[] rowm = new Object[3];
		        rowm[0] = rs.getInt("id_ingrediente");
		        rowm[1] = rs.getString("id_receta");
		        rowm[2] = rs.getString("gramos");

		        modelom.addRow(rowm);
		    }

		    rs.close();
		    stmt.close();
		

		} catch (SQLException ex) {
		    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		JTable tablaMatriz = new JTable(modelom);
		JScrollPane scrollPane3 = new JScrollPane(tablea);
		
		scrollPane3.setBounds(265, 12, 213, 156);
		frame.getContentPane().add(scrollPane3);
		
		JButton btnGuardarComida = new JButton("Guardar");
		btnGuardarComida.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement ps = con.prepareStatement("INSERT INTO comida VALUES (?, ?, ?)");
					
					
					ps.setInt(1, Integer.parseInt(textFieldIdIngrediente.getText()));
					ps.setInt(2,Integer.parseInt(textFieldIdReceta.getText()));
					ps.setInt(3,Integer.parseInt(textFieldGramos.getText()));

					ps.executeUpdate();
					ps.close();
					
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM comida");

				    while (rs.next()) {
				        Object[] rowm = new Object[2];
				        rowm[0] = rs.getInt("id_ingrediente");
				        rowm[1] = rs.getString("id_receta");
				        rowm[2] = rs.getString("gramos");

				        modelom.addRow(rowm);
				    }

				    rs.close();
				    stmt.close();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnGuardarComida.setBounds(321, 192, 105, 27);
		frame.getContentPane().add(btnGuardarComida);
		
		JButton btnActualizarComida = new JButton("Actualizar");
		btnActualizarComida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnActualizarComida.setBounds(321, 237, 105, 27);
		frame.getContentPane().add(btnActualizarComida);
		
		JButton btnBorrar_1 = new JButton("Borrar");
		btnBorrar_1.setBounds(321, 284, 105, 27);
		frame.getContentPane().add(btnBorrar_1);
		
		
		
		
		
		
	}
}