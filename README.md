# 🚗 Sistema de Cadastro de Veículos

Aplicação desktop para gerenciamento e cadastro de veículos desenvolvida em **Java 25** com interface em **JavaFX**, utilizando arquitetura **MVC** e o padrão **Repository** para persistência em **PostgreSQL**.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 25
* **Interface Gráfica:** JavaFX (renderização de telas e diálogos)
* **Gerenciador de Dependências:** Maven
* **Banco de Dados:** PostgreSQL 18 (persistência relacional)
* **Arquitetura:** MVC (Model-View-Controller) & Repository Pattern
* **Documentação Visual:** Mermaid (diagramas de classes e sequência)

---

## 🛡️ Validações e Regras de Negócio

Para garantir a consistência dos dados antes do envio ao banco de dados, o sistema executa verificações no momento do cadastro e da edição.

### 1. Validação de Campos Obrigatórios
Impede a entrada de valores `null` ou compostos apenas por espaços em branco nos campos de texto.

```java
if (nome == null || nome.trim().isEmpty() || 
    cor == null || cor.trim().isEmpty() || 
    modelo == null || modelo.trim().isEmpty()) {
    throw new IllegalArgumentException("Nenhum campo de texto pode ficar em branco!");
}
```

### 2. Conversão Númerica
Impede a entrada de valores alpha númericos ou letras no campo de ano.

```java
    try{
            anoParser = Integer.parseInt( anoStr );
        }catch( NumberFormatException nfe ){
            throw new IllegalArgumentException( "Digite um ano válido!" + nfe.getMessage() );
        }
```
### 3. Validação do Chassi
Impede a entrada de valores que não são baseados na norma ISO 3779( Excluindo as letras `I`, `O` e `Q` para evitar confusão com 1 e 0 ).

```java
    if (chassi == null || chassi.length() < 17) {
            throw new IllegalArgumentException("Tamanho inválido! Digite um tamanho de chassi corretamente. ");
    }

    String regexChassi = "^[A-HJ-NPR-Z0-9]{17}$";
    return chassi.toUpperCase().matches(regexChassi);
    
```
### 4. Validação da Placa
Impede a entrada de valores que não são baseados no formato tradicional do MercoSul ( `AAA1234` ou `AAA1A23` ).
```java
     if (placa == null || placa.length() < 7) {
            throw new IllegalArgumentException("Tamanho inválido! Uma placa deve ter 7 caracteres. ");
    }

    String regexPlaca = "^[A-Z]{3}[0-9]([A-Z]|[0-9])[0-9]{2}$";
    return placa.toUpperCase().matches( regexPlaca );
```


### REGEX utilizados
    > Para Chassi:
    `   
        if( chassi.length() < 17){
            throw new RuntimeException( "Tamanho inválido! Digite um tamanho de chassi corretamente! ");
        }

        // Pela minha experiência diária no setor automativo
        // E pelo que li em um forúm
        // Não é sempre permitido = acho que nem é - chassis iniciarem com 0
        String t1 = "^[A-HJ-NPR-Z0-9]{17}$";
        String t2 = chassi.toUpperCase();
        return t2.matches(t1);
    `
    > Para Placa:
    `
        if( placa.length() < 7 ){
            throw new RuntimeException( "Tamanho inválido! Digite um tamanho de placa corretamente!" );
        }
        
        String t1 = "^[A-Z]{3}[0-9]([A-Z]|[0-9])[0-9]{2}$";
        String t2 = placa.toUpperCase();
        return t2.matches(t1);
    `


# 🔄 Diagramas de Sequência

 # 1 - Criar Veículo
1. Criar Veículo
    Representa o fluxo de criação, validação, persistência no banco e backup auxiliar em arquivo de texto.

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
        DataBase -->> Controller: Retorna um sucesso ao salvar o veículo
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
        DataBase -->> Controller: Retorna uma falha ao salvar o veículo 
        Controller ->> DataBase: Executa a exclusão do veículo recém criado ( ROLLBACK MANUAL ) e instância uma exceção
        Controller -->> Interface: Lança uma exceção
        Interface -->> User: Exibe uma mensagem de falha

    end
```

# 2 - Editar Veículo
Fluxo de alteração das informações de um veículo existente.

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

    DataBase -->> Controller: Retorna uma informação sobre a atualização( Sucesso ou Falha )

    alt VeiculoAtualizado
        Controller -->> Interface: Retorna um sucesso
        Interface -->> User: Exibe uma mensagem de sucesso 

    else VeiculoNaoAtualizado
        Controller -->> Interface: Retorna uma falha
        Interface -->> User: Exibe uma mensagem de falha
    end

```
# 3 - Excluir Veículo
Fluxo de remoção de veículo por ID.

```mermaid
---
config: 
    theme:dark
---

sequenceDiagram
    autonumber
    actor User

    participant Interface
    participant Controller
    participant DataBase

    User ->> Interface: Seleciona uma linha e clica em 'Excluir'
    Interface -->> User: Exibe um pop-up de confirmação pedindo a confirmação da exclusão
    User -->> Interface: Confirma a exclusão do veículo

    Interface ->> Controller: Recebe e envia o ID do veículo para o DataBase
    Controller ->> DataBase: Envia o ID para o DataBase
    alt EsseIdExiste
        DataBase ->> DataBase: Exclui o ID
        DataBase -->> Controller: Retorna True
        Controller -->> Interface: Captura o retorno
        Interface -->> User: Exibe uma mensagem sobre o sucesso da exclusão do veículo

    else IdNaoExiste
        DataBase ->> DataBase: Lança uma exceção
        DataBase ->> Controller: Retorna a exceção para a interface
        Controller ->> Interface: Captura a exceção
        Interface ->> User: Lança uma mensagem sobre uma falha ao tentar excluir o veículo
    end
```

# 📐 Diagrama de Classes
Estrutura das classes e pacotes (models, controller, repository e view).

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