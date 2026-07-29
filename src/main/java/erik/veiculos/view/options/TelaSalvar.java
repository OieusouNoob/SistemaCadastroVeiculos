package erik.veiculos.view.options;

import erik.veiculos.controller.VeiculoController;
import erik.veiculos.models.Veiculo;
import erik.veiculos.utills.javafxutils.JavaFXUI;
import erik.veiculos.view.options.pesquisar.TelaPesquisar;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



public class TelaSalvar {

    private final TextField campoNome;
    private final TextField campoCor;
    private final TextField campoAno;
    private final TextField campoModelo;
    private final TextField campoChassi;
    private final TextField campoPlaca;

    private final Button botaoSalvar;
    private final Button botaoVoltar;

    private final CheckBox caixaSimNao;


    public TelaSalvar(){

        campoNome = JavaFXUI.criarCampos("Nome");
        JavaFXUI.setLimitChars( campoNome, 15 );

        campoCor = JavaFXUI.criarCampos("Cor");
        JavaFXUI.setLimitChars( campoCor, 15 );

        campoAno = JavaFXUI.criarCampos("Ano");
        JavaFXUI.setLimitChars(campoAno, 4);

        campoModelo = JavaFXUI.criarCampos("Modelo");
        JavaFXUI.setLimitChars( campoModelo, 15 );

        campoChassi = JavaFXUI.criarCampos("Chassi");
        JavaFXUI.setLimitChars( campoChassi, 17 );

        campoPlaca = JavaFXUI.criarCampos("Placa");
        JavaFXUI.setLimitChars( campoPlaca, 7 );


        botaoSalvar = new Button("Salvar");
        botaoVoltar = new Button("Voltar");

        caixaSimNao = new CheckBox("Único Dono? ");
    }
    public Pane getFormularioSalvar(BorderPane telaPrincipal, Veiculo car ) {

        if( telaPrincipal == null ){
            throw new RuntimeException("Falha ao criar a tela principal!");
        }

        HBox layoutOpcao = new HBox( 10, caixaSimNao );
        caixaSimNao.setAlignment( Pos.BOTTOM_CENTER );

        VBox layout = new VBox(25);
        layoutOpcao.setAlignment( Pos.BOTTOM_CENTER );


        HBox buttons = new HBox( 30, botaoVoltar, botaoSalvar );
        buttons.setAlignment( Pos.BOTTOM_CENTER );

        botaoSalvar.setOnAction(_ -> {

            if ( JavaFXUI.AlertaVazio( campoNome, campoCor, campoAno, campoModelo, campoChassi, campoPlaca ) ){
                return;
            }

            try{
                boolean sucesso = VeiculoController.salvarOuAtualizar(
                        car,
                        campoNome.getText(),
                        campoCor.getText(),
                        campoAno.getText(),
                        campoModelo.getText(),
                        campoChassi.getText(),
                        campoPlaca.getText(),
                        caixaSimNao.isSelected()
                );
                if( sucesso ){
                    JavaFXUI.Alertas( Alert.AlertType.INFORMATION, "Sucesso", "Dados salvos com sucesso!");
                }
            }catch( RuntimeException e){
                JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha interna", e.getMessage() );
            }

        });

        botaoVoltar.setOnAction(a -> { // Ação do botão voltar
            new TelaPesquisar().telaPesquisar( telaPrincipal );
        });

        if( car != null ){ // Atualizar os dados de um veículo
            campoNome.setText( car.getNome() );
            campoCor.setText( car.getCor() );
            campoAno.setText( car.getAno() + "" ); //Vai assim mesmo
            campoModelo.setText( car.getModelo() );
            campoChassi.setText( car.getNumeroChassi() );
            campoPlaca.setText( car.getPlaca() );

        }

        //Renderização e organização dos componentes na tela
        layout.getChildren( ).addAll(
                campoNome,
                campoCor,
                campoAno,
                campoModelo,
                campoChassi,
                campoPlaca,
                layoutOpcao,
                buttons
        );

        layout.setAlignment( Pos.CENTER );
        return layout;
    }
}