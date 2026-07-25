package erik.veiculos.view.options.pesquisar;

import java.util.List;

import erik.veiculos.controller.VeiculoController;
import erik.veiculos.models.Veiculo;
import erik.veiculos.utills.Utills;
import erik.veiculos.utills.javafxutils.JavaFXUI;
import erik.veiculos.view.options.TelaSalvar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;



public class TelaPesquisar {

    private final TableColumn<Veiculo, Integer> colunaId = new TableColumn<>("Id");
    private final TableColumn<Veiculo, String> colunaNome = new TableColumn<>( "Nome" );
    private final TableColumn<Veiculo, String> colunaCor = new TableColumn<>("Cor");
    private final TableColumn<Veiculo, String> colunaAno = new TableColumn<>( "Ano" );
    private final TableColumn<Veiculo, String> colunaModelo = new TableColumn<>( "Modelo" );
    private final TableColumn<Veiculo, String> colunaChassi = new TableColumn<>( "Chassi" );
    private final TableColumn<Veiculo, String> colunaPlaca = new TableColumn<>( "Placa" );
    private final TableColumn<Veiculo, Boolean> colunaUnicoDono = new TableColumn<>( "Dono Único" );


    private final Button btnAlterar;
    private final Button btnExcluir;
    private final Button btnSalvarNovo;
    private final Button btnBuscar;
    private final Button btnAnterior;
    private final Button btnProximo;

    private final TextField campoBusca;

    private final ComboBox<Pair<String, String>> caixaSuspensaFiltro = JavaFXUI.opcoesComboBox();
    private final ComboBox<Utills.FiltroDono> caixaSuspensaUnicoDono = new ComboBox<>();

    private int offSet = 0;


    public TelaPesquisar(){

        colunaId.setCellValueFactory( new PropertyValueFactory<>( "id" ) );
        colunaNome.setCellValueFactory( new PropertyValueFactory<>( "nome" ) );
        colunaCor.setCellValueFactory( new PropertyValueFactory<>( "cor" ) );
        colunaAno.setCellValueFactory( new PropertyValueFactory<>( "ano" ) );
        colunaModelo.setCellValueFactory( new PropertyValueFactory<> ( "modelo" ) );
        colunaChassi.setCellValueFactory( new PropertyValueFactory<>("numeroChassi") );
        colunaPlaca.setCellValueFactory( new PropertyValueFactory<>( "placa" ) );
        colunaUnicoDono.setCellValueFactory( new PropertyValueFactory<>( "unicoDono" ) );

        btnAlterar = new Button( "Alterar" );
        btnExcluir = new Button( " Excluir" );
        btnSalvarNovo = new Button("Salvar/Novo");
        btnBuscar = new Button("Buscar");
        btnAnterior = new Button("Anterior");
        btnProximo = new Button("Próximo");

        campoBusca = new TextField();
        JavaFXUI.setLimitChars( campoBusca, 20 );
        campoBusca.setPromptText("Pesquise algo");

        //Criamos uma caixa suspensa para o utilizador escolher se é único dono
        caixaSuspensaUnicoDono.getItems().addAll( Utills.FiltroDono.values() );

        caixaSuspensaUnicoDono.setConverter(new StringConverter<>() {
            @Override
            public String toString(Utills.FiltroDono filtroDono) {
                // Esse if abaixo é desnecessário, se não 'Sim' nem 'Não' eu caio em 'Ambos' da mesma forma
                //if( filtroDono == null ) return "";
                if( filtroDono == Utills.FiltroDono.SIM ) return "Sim";
                if( filtroDono == Utills.FiltroDono.NAO) return "Não";
                return "Ambos";
                /*Essas linhas acima é para exibir o texto que o utilizador vai clicar,
                 * para ele vai aparecer apenas "Sim", "Não" e "Ambos".
                 * Para o backend da aplicação vai ser o próprio filtro*/
            }
            /*
            * Esse trecho acima funciona assim:
            * Ao declararmos uma ENUM em Java ela também se torna um objeto, mas um objeto CONSTANTE
            * Esta última parte é uma boa para que eu possa descobrir qual foi a opção que a pessoa selecionou
            * porque se eu tenho três opções constantes sempre vou ter um final previsível para lidar.
            * Isso facilita bastante o controle de fluxo de dados aqui na View
            *
            * */

            @Override
            public Utills.FiltroDono fromString(String s) {
                return null;
            }
        });

        // Enquanto realiza algumas pesquisas e perguntas eu fiquei indignado com que aparecia apenas o 'true' ou 'false'
        /*
        * Quero que apareça 'Sim' e 'Não'.
        *
        * E com esta indignação este trecho abaixo foi criado
        * */

        /*
        * Este trecho apenas descobre o que estar armazenado na coluna unicoDono de Veículo e exibi 'Sim' ou 'Não'
        *  Contudo, quem está fazendo isso é a própria coluna, antes eu pensava que era responsabilidade da
        *  TableView, mas, aparentemente a própria coluna tem que saber sobre si mesma, realmente tudo é um objeto.
        *   */
        colunaUnicoDono.setCellFactory( colunaDono -> new TableCell<>(){
            @Override
            protected void updateItem( Boolean item, boolean empty ){// Item é o dado que precisamos, empty é a flag
                super.updateItem( item, empty ); // Chamada a classe mãe/pai para descobrir se tem o objeto na memória
                // Se tiver item será diferente de null, e se empty for true é porque acabou a nossa lista de objetos
                if( empty || item == null ){ // Se empty tem algo true ou o item é igual a null...
                    // Não colocamos nada na tabela!
                    setText( null );
                }else{ // Ainda tem algo em item e empty é false! No caso não está vazio.
                    setText( item ? "Sim" : "Não" );
                }
            }
        });
        caixaSuspensaUnicoDono.getSelectionModel().selectFirst(); //Deixamos o primeiro selecionado por padrão


    }


    private void atualizarTabela(ObservableList<Veiculo> dadosTable){

        Pair<String, String> selecionado = caixaSuspensaFiltro.getValue();
        List<Veiculo> car;

        //Toda busca é uma busca filtrada, então não faz sentido a não ser para o começo o método buscarGeral!
        if (offSet >= 0 && campoBusca.getText().isEmpty()) {
            // Se for passado 'null' como segundo parâmetro vai ser entendido que é uma busca sem texto, apenas pela seleção do 'id', 'nome', '...' etc.
            car = VeiculoController.buscarComFiltro( selecionado.getValue(), null, caixaSuspensaUnicoDono.getValue(), offSet );

        } else {
            car = VeiculoController.buscarComFiltro( selecionado.getValue(), campoBusca.getText(), caixaSuspensaUnicoDono.getValue(), offSet );

        }
        btnProximo.setDisable( car.size() < 15 ); // Verdade? Desabilita!
        btnAnterior.setDisable( offSet <= 0 ); //Menor por precaução

        dadosTable.clear(  );
        dadosTable.addAll( car );
    }

    public Pane telaPesquisar( BorderPane telaPrincipal ) {

        if( telaPrincipal == null ){
            throw new IllegalArgumentException("Falha na criação da tela!");
        }
        VBox layout = new VBox( 10 );

        layout.setStyle("-fx-padding: 20px;");

        HBox barraBusca = new HBox(30);

        Label textDono = new Label("Único Dono: ");

        barraBusca.getChildren().addAll( campoBusca, btnBuscar, caixaSuspensaFiltro , textDono , caixaSuspensaUnicoDono );

        TableView<Veiculo> tabelaVeiculo = new TableView<>();
        tabelaVeiculo.setFixedCellSize( 40.0 ); // Altura das linhas - para evitar ‘bugs’ visuais

        //Para evitar erros de layout, por exemplo...
        /*
         * Fiz uns testes e notei que quando estava em tela cheia a tabela ficava cortada como se fosse ainda
         * uma tela dividida em 1.5
         * */
        VBox.setVgrow( tabelaVeiculo, Priority.ALWAYS ); // Corrigido o problema da tela pela metade
        // Esta linha acima faz um alinhamento em vertical segundo o dimensionamento da tela

        tabelaVeiculo.getColumns().addAll(List.<TableColumn<Veiculo, ?>> of(
                colunaId,
                colunaNome,
                colunaCor,
                colunaAno,
                colunaModelo,
                colunaChassi,
                colunaPlaca,
                colunaUnicoDono
        ));

        tabelaVeiculo.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS );

        // ObservableList foi escolhida porque enquanto eu pesquisava, perguntava e lia respostas ele pareceu ser melhor
        // para resolver o meu problema em algumas coisas. Como o caso .setItems()

        // Na documentação do JavaFX foi usado um ArrayList, mas aqui eu senti a necessidade de algo diferente
        // Por isso foi escolhido a ObservableList, queria aprender como usar, haha. No fim é só um array mais lento, haha.
        ObservableList< Veiculo > dadosTable = FXCollections.observableArrayList( );

        tabelaVeiculo.setItems( dadosTable );
        Pair< String, String > temp = caixaSuspensaFiltro.getValue();
        
        List<Veiculo> carTemp = VeiculoController.buscarComFiltro( temp.getValue(), null, caixaSuspensaUnicoDono.getValue(), offSet );

        if( carTemp.isEmpty() ) {

            JavaFXUI.Alertas(Alert.AlertType.ERROR, "Vazio", "Nenhum veículo encontrado!");

        }

        dadosTable.addAll(carTemp);

        HBox botoes = new HBox(35, btnAnterior, btnSalvarNovo, btnAlterar, btnExcluir, btnProximo );
        botoes.setAlignment( Pos.BOTTOM_CENTER );

        //Vamos desativar os botões até que seja selecionado alguma linha na 'TableView'
        //Pegando o momento da seleção de alguma linha, para permitimos o botão aparecer
        btnAlterar.disableProperty().bind( tabelaVeiculo.getSelectionModel().selectedItemProperty().isNull() );
        btnExcluir.disableProperty().bind( tabelaVeiculo.getSelectionModel().selectedItemProperty().isNull() );
        btnAnterior.setDisable( offSet <= 0 );
        //Vamos desabilitar o botão de 'voltar' caso o utilizador tenha apenas, ou esteja, na primeira página

        btnBuscar.setOnAction( buscar -> {
            /*
            * Notei que criei o método para atualizar a tabela, mas nunca é usado onde deveria! Então vamos refatorar essa reação do 'buscar'
            *                      */
            // Toda nova busca vai resetar o 'offSet' para 0.
            offSet = 0;
            try {
                atualizarTabela(dadosTable);
            }catch ( IllegalArgumentException e ){
                JavaFXUI.Alertas( Alert.AlertType.ERROR, "Busca Inválida!", e.getMessage() );
            }catch( RuntimeException e ){
                JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha Interna", e.getMessage() );
            }
        });

        btnAlterar.setOnAction( alterar -> {
            Veiculo car = tabelaVeiculo.getSelectionModel().getSelectedItem();
            telaPrincipal.setCenter( new TelaSalvar().getFormularioSalvar( telaPrincipal, car ) );

        });

        btnExcluir.setOnAction( excluir -> {
            Veiculo car = tabelaVeiculo.getSelectionModel().getSelectedItem(); // Recebendo o car selecionado (endereço dele na memória, já que os objetos em java são apenas referências - ponteiros)

            if( JavaFXUI.AlertaSimNao("Excluir Veículo", "Tem certeza que deseja excluir este veículo? Esta operação não poderá ser desfeita!" ) ){
                try{
                    boolean resultado = VeiculoController.excluirVeiculo( car.getId( ) );

                    if( resultado ) {
                        JavaFXUI.Alertas(Alert.AlertType.INFORMATION, "Sucesso", "Veículo excluído com sucesso!");
                        dadosTable.remove(car); // Passando o objeto para que com toda a certeza o endereço do objeto seja igual ao que eu preciso apagar no momento.
                        // Um dos motivos de ter usado a ObserverList foi esse método de remoção! Apesar que no fim não muda tanto
                    }

                }catch(RuntimeException e){
                    JavaFXUI.Alertas( Alert.AlertType.ERROR, "Error", e.getMessage() );
                }
            }

        });

        btnSalvarNovo.setOnAction(salvar -> {
            try{
                telaPrincipal.setCenter(  new TelaSalvar().getFormularioSalvar( telaPrincipal, null ) );
                offSet = 0;
                // Começamos do 0 com o offSet para que todas vezes que atualizamos a tela, ela não de erros como
                // botões ativos estando no limite já permitindo o utilizador clicar, não vai atualizar, mas vai mostrar que é possível clicar
                atualizarTabela( dadosTable );
            }catch (RuntimeException e){
                JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha na Tela", e.getMessage() );
            }

        });

        btnProximo.setOnAction(avancar -> {
            offSet += 15;
            try {
                atualizarTabela(dadosTable);
            }catch ( IllegalArgumentException e ){
                JavaFXUI.Alertas( Alert.AlertType.ERROR, "Busca Inválida!", e.getMessage() );
            }catch( RuntimeException e ){
                JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha Interna", e.getMessage() );
            }

        });

        btnAnterior.setOnAction(voltar -> {

            if( offSet >= 15){
                offSet -= 15;
                try {
                    atualizarTabela(dadosTable);
                }catch ( IllegalArgumentException e ){
                    JavaFXUI.Alertas( Alert.AlertType.ERROR, "Busca Inválida!", e.getMessage() );
                }catch( RuntimeException e ){
                    JavaFXUI.Alertas( Alert.AlertType.ERROR, "Falha Interna", e.getMessage() );
                }
            }
        });


        btnProximo.setAlignment( Pos.BOTTOM_RIGHT );
        btnAnterior.setAlignment( Pos.BOTTOM_LEFT );

        layout.getChildren().addAll(
                barraBusca,
                tabelaVeiculo,
                botoes
        );

        telaPrincipal.setCenter( layout );

        return telaPrincipal;
    }
}
