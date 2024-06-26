## Integrantes

- Murilo Lopes 202320186
- Renan Souza Borges 202410864
- Yann Vinnycius de Lima Matias 324122109
- Lucas Sant’Anna Ramos 202321698
- João Victor França 202320139

## Metodologia 

1. Definição do Problema
Nosso objetivo é desenvolver um sistema de recomendação de músicas que sugira playlists personalizadas para os usuários com base em suas avaliações. O sistema irá imprimir músicas aleatória para que o usuario avalia-as, o gênero que recebeu a melhor avaliação do usuário será o principal gênero presente na playlist.

2. Coleta de Dados
Base de Dados de Músicas: Obter um dataset contendo músicas, gêneros, e outras informações relevantes (e.g., artista, álbum).
Avaliações dos Usuários: Registrar as avaliações fornecidas pelos usuários para diferentes músicas.
Playlist: Armazena as playlists geradas para cada usuario para acesso posterior.
Usuario: Contém as informações pessoais do usuário.

3. Pré-processamento de Dados
Limpeza de Dados: Remover dados inconsistentes ou duplicados.
Normalização das Avaliações: Assegurar que todas as avaliações estejam em um formato consistente (e.g., escala de 1 a 5).
4. Desenvolvimento do Algoritmo de Recomendação
4.1. Estrutura do Projeto
Modelos: Classes que representam as entidades principais (e.g., Musica, Usuario, Avaliacao).
Serviços: Classes que encapsulam a lógica de negócio (e.g., RecomendacaoService).
Persistência: Interface para acessar os dados (e.g., MusicaRepository, AvaliacaoRepository).
4.2. Implementação


