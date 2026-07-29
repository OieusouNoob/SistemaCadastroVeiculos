# SistemaCadastroVeiculos

# Tecnologia Usadas

    - Mermaid: Criação de Diagramas de Classes e Sequência
    - Padrão MVC: Aproveitando a separação de responsabilidades para um futuro escalonamento
    - Java 25
    - Maven
    - JavaFX: Renderização de telas e pop-ups
    - PostgreSQL 18: Armazenamento de longo prazo


# Diagrama de Sequência

 # 1 - Criar Veículo
```mermaid
---
config:
  theme: dark
---
sequenceDiagram
    autonumber

    actor User

    participant  Interface
    participant  Controller
    participant  DataBase
    participant  Utills

    User ->> Interface: Clicar em Salvar/Novo
    Interface -->> User: Devolve um formulário para preenchimento
    User ->> Interface: Informa os dados no formulário e clica em 'Salvar'
    Interface ->> Controller: Valida os dados
    Controller ->> DataBase: Salva os dados
  

    alt VeiculoSalvo
        Controller ->> Utills: Salva os dados em um arquivo .txt
        Utills -->> Controller: Informa se o veículo foi salvo no arquivo .txt

        alt VeiculoSalvoArquivo
            Controller -->> Interface: Retorna um sucesso
            Interface -->> User: Exibe uma mensagem de sucesso
        else
            Controller -->> Interface: Retorna uma falha
            Interface -->> User: Exibe uma mensagem de falha
        end
    else VeiculoNaoSalvo
        Controller ->> DataBase: Executa a exclusão do veículo recém criado ( ROLLBACK MANUAL ) e instância uma exceção
        Controller -->> Interface: Lança uma exceção
        Interface -->> User: Exibe uma mensagem de 

    end
```

# 2 - Editar Veículo
```mermaid
---
config: 
    theme: dark
---
sequenceDiagram
    autonumber

    actor User

    participant Interface
    participant Controller
    participant DataBase

    User ->> Interface: Seleciona o veículo desejado e clica em 'Alterar'

    Interface -->> User: Devolve um formulário preenchido com as informações do veículo selecionado

    User ->> Interface: Insere os novos dados e clica em 'Salvar'

    Interface ->> Controller: Valida os dados do veículo

    Controller ->> DataBase: Obtém o 'ID' do veículo da instância original e atualiza os dados

    DataBase -->> Controller: Retorna a informação da atualização das informações do veículo

    alt VeiculoAtualizado
        Controller -->> Interface: Retorna um sucesso
        Interface -->> User: Exibe uma mensagem de sucesso 

    else VeiculoNaoAtualizado
        Controller -->> Interface: Retorna uma falha
        Interface -->> User: Exibe uma mensagem de falha
    end

```


# Diagrama de Classes 

```mermaid
---
config:
  theme: dark
---
classDiagram
    namespace models {
        class Veiculo {
            -int id
            -String nome
            -String cor
            -int ano
            -String modelo
            -String numeroChassi
            -String placa
            -boolean unicoDono
            +Veiculo()
            +Veiculo(int, String, String, int, String, String, String, boolean)
            +getters_setters()
        }
    }
    namespace controller {
        class VeiculoController {
            -VeiculoController()
            +salvarOuAtualizar(Veiculo, String, String, String, String, String, String, boolean)\$ boolean
            +buscarComFiltro(String, String, FiltroDono, int) List~Veiculo~
            +excluirVeiculo(int) boolean
        }
    }
    namespace repository {
        class VeiculoRepository {
            -String INS
            -String UPT
            -String DEL
            -VeiculoRepository()
            +salvar(Veiculo) boolean
            +pesquisar(String, String, FiltroDono, int) List~Veiculo~
            +update(Veiculo) boolean
            +delete(int) boolean
        }
    }
    namespace view {
        class TelaPrincipal {
            +start(Stage stage) void
        }
        class TelaPesquisar {
            -TableColumn colunaId
            -TableColumn colunaNome
            -TextField campoBusca
            -Button btnBuscar
            -int offSet
            +TelaPesquisar()
            +telaPesquisar(BorderPane) Pane
            -atualizarTabela(ObservableList~Veiculo~) void
        }
    }

    namespace view{
        class TelaSalvar{
            -TextField campoNome
            -TextField campoCor
            -TextField campoAno
            -TextField campoModelo
            -TextField campoChassi
            -TextField campoPlaca
            -Button botaoSalvar
            -Button botaoVoltar
            -CheckBox caixaSimNao
            +TelaSalvar()
            +Pane getFormularioSalvar(BorderPane, Veiculo)
        }
    }

    TelaPrincipal ..> TelaPesquisar : Instancia e Inicia
    TelaPesquisar ..> VeiculoController : Envia dados da UI
    TelaPesquisar ..> TelaSalvar : Instancia Tela de salvamento
    TelaPesquisar ..> Veiculo : Exibe na Tabela
    VeiculoController ..> VeiculoRepository : Delega persistência
    VeiculoController ..> Veiculo : Valida e Instancia
    VeiculoRepository ..> Veiculo : Preenche e Retorna

```

