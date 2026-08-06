package erik.veiculos.controller;

import java.io.IOException;
import java.util.List;

import erik.veiculos.models.Veiculo;
import erik.veiculos.repository.VeiculoRepository;
import erik.veiculos.utills.Utills;

public class VeiculoController {

    private VeiculoController() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean salvarOuAtualizar(Veiculo carroOriginal, String nome, String cor, String anoStr, String modelo, String chassi, String placa, boolean unicoDono )  {
        int anoParser;
        if (nome == null || nome.trim().isEmpty() || cor == null || cor.trim().isEmpty() || modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("Nenhum campo de texto pode ficar em branco!");
        }
        try{
            anoParser = Integer.parseInt( anoStr );
        }catch( NumberFormatException nfe ){
            throw new IllegalArgumentException( "Digite um ano válido!" + nfe.getMessage() );
        }

        if( !Utills.ehChassi( chassi ) ){
            throw new IllegalArgumentException( "Chassi inválido ou com menos de 17 caracteres!" );
        }

        if( !Utills.ehPlaca( placa ) ){
            throw new IllegalArgumentException( "Placa inválida ou com menos de 7 caracteres!" );
        }
        Veiculo carro = new Veiculo();

        carro.setNome( nome );
        carro.setCor( cor );
        carro.setAno( anoParser );
        carro.setModelo( modelo );
        carro.setNumeroChassi( chassi );
        carro.setPlaca ( placa );
        carro.setUnicoDono( unicoDono );

        boolean sucesso;
        boolean saveSuccess = false;

        try {
            if( carroOriginal != null ){ // Se carro não é null, é para atualizar, apenas retornamos true
                carro.setId( carroOriginal.getId() );
                sucesso = VeiculoRepository.update( carro );
                if( !sucesso ) {
                    throw new RuntimeException( "Falha ao atualizar o veículo!" );
                }
                return sucesso;
            }
            sucesso = VeiculoRepository.salvar( carro );

            if ( sucesso ) {

                saveSuccess = Utills.salvarArquivo( carro );

            }

            if ( !saveSuccess ) {
                VeiculoRepository.delete( carro.getId() );
                throw new RuntimeException( "Falha ao salvar o arquivo do veículo, essa operação cancelada!" );
            }
        // A linha abaixo é uma soma de exceções que podem ser lançadas. 
        }catch( IOException | RuntimeException e ){ // IllegalArgumentException é uma sub-classe de RuntimeException - Todos edges cases com respostas!.
            throw new RuntimeException( e.getMessage(), e );
        }

        return sucesso && saveSuccess; // Realizamos um teste lógico para saber se foi preciso fazer um rollback ou não, 
        // caso o arquivo não tenha sido salvo, deletamos o carro do banco de dados.
    }

    public static List<Veiculo> buscarComFiltro( String coluna, String valorDigitado, Utills.FiltroDono unicoDono, int offSet ){
        return VeiculoRepository.pesquisar( coluna, valorDigitado, unicoDono, offSet );
    }
    
    public static boolean excluirVeiculo( int id  ){
        return VeiculoRepository.delete( id );
    }
}
