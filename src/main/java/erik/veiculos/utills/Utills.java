package erik.veiculos.utills;

import java.awt.Dimension;
import java.awt.Toolkit;

import erik.veiculos.models.Veiculo;



public class Utills {

    private Utills() {
        throw new UnsupportedOperationException("Está classe não pode ser instanciada");
    }


    public enum FiltroDono{
        AMBOS,
        SIM,
        NAO,
    }


    public static boolean ehChassi(String chassi){
        if (chassi == null || chassi.length() < 17) {
            throw new IllegalArgumentException("Tamanho inválido! Digite um tamanho de chassi corretamente. ");
        }

        //Pela minha experiência diária no setor automativo
        // E pelo que li em um forúm
        // Não é sempre permitido = acho que nem é - chassis iniciarem com 0
        String regexChassi = "^[A-HJ-NPR-Z0-9]{17}$";
        return chassi.toUpperCase().matches(regexChassi);
    }

    public static boolean ehPlaca(String placa) {
        if (placa == null || placa.length() < 7) {
            throw new IllegalArgumentException("Tamanho inválido! Uma placa deve ter 7 caracteres. ");
        }

        String regexPlaca = "^[A-Z]{3}[0-9]([A-Z]|[0-9])[0-9]{2}$";
        return placa.toUpperCase().matches( regexPlaca );
    }

    public static Dimension sizeScreen() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    } //Sei que não é a melhor forma, mas funciona

    public static boolean salvarArquivo( Veiculo car ) throws java.io.IOException{

        if ( car == null ){
            throw new RuntimeException( "Erro! O carro não existe!" );
        }

        try (java.io.PrintWriter escrever = new java.io.PrintWriter( new java.io.FileWriter("veiculo.txt", true) )){
            escrever.printf(
                    "Veículo (ID) %d%n Nome: %s%n Cor: %s%n Ano: %d%n Modelo: %s%n Chassi: %s%n Placa: %s%n Único Dono? %s%n-----------------------------------%n",
                    car.getId(), // Bug corrigido! Antes puxava do car o 'id', mas car nunca possui o seu próprio ‘id’, infelizmente tive que extrai-lo a parte
                    car.getNome(),
                    car.getCor(),
                    car.getAno(),
                    car.getModelo(),
                    car.getNumeroChassi(),
                    car.getPlaca(),
                    car.isUnicoDono() ? "Sim" : "Não"
            );

            System.out.println("Arquivo salvo em: " + new java.io.File("veiculo.txt").getAbsolutePath());

            return true;
        }catch( java.io.IOException e ){
            throw new RuntimeException("Error ao salvar o arquivo do veículo! ");
        }
    }



}

