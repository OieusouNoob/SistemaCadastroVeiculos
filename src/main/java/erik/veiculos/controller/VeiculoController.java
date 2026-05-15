package erik.veiculos.controller;

import erik.veiculos.models.Veiculo;
import erik.veiculos.repository.VeiculoRepository;
import erik.veiculos.utills.Utills;

import java.io.IOException;
import java.util.List;

public class VeiculoController {

    public static boolean salvarOuAtualizar(Veiculo carroOriginal, String nome, String cor, String anoStr, String modelo, String chassi, String placa, boolean unicoDono )  {
        int anoParser;
        try{
            anoParser = Integer.parseInt( anoStr );
        }catch( NumberFormatException nfe ){
            throw new IllegalArgumentException( "Digite um ano corretamente!" + nfe.getMessage() );
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

        if( carroOriginal != null ){
            carro.setId( carroOriginal.getId() );
            sucesso = VeiculoRepository.update( carro );
            saveSuccess = true;
        }else{
            try {
                sucesso = VeiculoRepository.salvar( carro );

                if ( sucesso ) {

                    saveSuccess = Utills.salvarArquivo( carro );

                }
            }catch(IllegalArgumentException | IOException e ){
                throw new RuntimeException( e.getMessage(), e );
            }
        }
        return sucesso && saveSuccess; // If return is true, ok! anything different is false!
        // taking both results and return result finally!
    }



    public static List<Veiculo> buscarComFiltro( String coluna, String valorDigitado, Utills.FiltroDono unicoDono, int offSet ){
        return VeiculoRepository.pesquisar( coluna, valorDigitado, unicoDono, offSet );
    }

    public static boolean excluirVeiculo( int id  ){
        return VeiculoRepository.delete( id );
    }
}
