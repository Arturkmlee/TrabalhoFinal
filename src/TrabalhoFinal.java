import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TrabalhoFinal extends JFrame implements ActionListener {
    Connection con;
    Statement stmt;

    JPanel painelPrincipal = new JPanel(new GridLayout(3, 1));
    JButton cria, insere, visualiza;
    JLabel nome = new JLabel("DOCERIA DOM RAFAEL");

    public TrabalhoFinal(){
        super("Trabalho Final JDBC");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        criaPrincipal();
        iniciaBD();

        add(nome);
        add(painelPrincipal);
        pack();
        setVisible(true);
    }

    public void criaPrincipal(){
        cria = new JButton("Criar Banco de Dados");
        insere = new JButton("Inserir dados");
        visualiza = new JButton("Visualizar dados");

        painelPrincipal.add(cria);
        painelPrincipal.add(insere);
        painelPrincipal.add(visualiza);

        cria.addActionListener(this);
        insere.addActionListener(this);
        visualiza.addActionListener(this);
    }

    public void iniciaBD(){
        try{
            Class.forName("org.hsql.jdbcDriver");
            con = DriverManager.getConnection("jdbc:HypersonicSQL:hsql://localhost:8080", "sa", "");
            stmt = con.createStatement();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "O drive de Banco de Dados não foi encontrado.\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na iniciação do acesso ao banco de dados\n"+ex, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(cria.equals(e.getSource())){
            try{
                stmt.executeUpdate("CREATE TABLE DOCERIA (NOME VARCHAR(30), VALOR INT, QUANTIDADE INT)");
                JOptionPane.showMessageDialog(null, "A tabela foi criada com sucesso.", "Tabela criada", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro na criação da tabela.\n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Erro: \n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (insere.equals(e.getSource())){
            System.out.println("Botao Insere");
            new JanelaInsercao(con);
        }
        else if (visualiza.equals(e.getSource())){
            System.out.println("Botao Visualiza");
            new JanelaVisualizacao(con);
        }
    }

    public static void main(String[] args) {
        new TrabalhoFinal();
    }
}

class JanelaInsercao extends JFrame{
    PreparedStatement pStmt;
    JPanel nomeProd, valorProd, qtdProd;
    JLabel nomeLabel, valorLabel, qtdLabel;
    JTextField nome, valor, qtd;
    JButton insere;

    public JanelaInsercao(Connection con){
        super("Insere na tabela");
        setLayout(new GridLayout(4, 1));
        componentes();

        try {
            pStmt = con.prepareStatement("INSERT INTO TESTE DOCERIA (?,?,?)");
            insere.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        pStmt.setString(1, nome.getText());
                        pStmt.setInt(2, Integer.parseInt(valor.getText()));
                        pStmt.setInt(3, Integer.parseInt(qtd.getText()));
                        nome.setText("");
                        valor.setText("");
                        qtd.setText("");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro no SQL: \n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
        }

        pack();
        setVisible(true);
    }

    public void componentes(){
        nomeProd = new JPanel(new FlowLayout());
        nomeLabel = new JLabel("Nome do produto: ");
        nome = new JTextField(30);
        nomeProd.add(nomeLabel);
        nomeProd.add(nome);

        valorProd = new JPanel(new FlowLayout());
        valorLabel = new JLabel("Valor do produto: ");
        valor = new JTextField(30);
        valorProd.add(valorLabel);
        valorProd.add(valor);

        qtdProd = new JPanel(new FlowLayout());
        qtdLabel = new JLabel("Quantidade do produto: ");
        qtd = new JTextField(30);
        qtdProd.add(qtdLabel);
        qtdProd.add(qtd);

        add(nomeProd);
        add(valorProd);
        add(qtdProd);
        add(insere = new JButton("Insere"));
    }

}

class JanelaVisualizacao extends JFrame{
    PreparedStatement pStmt;

    public JanelaVisualizacao(Connection con){
        super("Visualizacao de dados");
        try{
            pStmt = con.prepareStatement("SELECT * FROM DOCERIA");
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()){
                String s = rs.getString(1);
            }
        } catch(Exception e){

        }
    }
}

