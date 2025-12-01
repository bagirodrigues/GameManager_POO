# GameManager - Gerenciador de Coleção de Jogos

**Tema escolhido**: Gerenciamento de coleção de jogos por ser um assunto de interesse pessoal e por permitir integrar diferentes tecnologias de forma prática.

## Funcionalidades
-  **Operações CRUD completas**
    * Criar, Ler, Atualizar e Excluir jogos;
    * Validação de dados e tratamento de erros.
   
-  **Integração com API RAWG.io**
   * Busca automática de informações de jogos;
   * Imagens, datas de lançamento, gêneros e avaliações.

-  **Interface gráfica** 
   * Cards visuais para cada jogo;
   * Tema personalizado;
   * Navegação fácil e responsiva.

- **Persistência de Dados**
  * Banco de dados SQLite local;
  * Backup automático da coleção;
  * Schema versioning automático.

- **Recursos Avançados**
   * Busca por título, gênero e plataforma;
   * Sistema de avaliação por estrelas;
   * Carregamento assíncrono de imagens;
   * Validação de URLs e dados.


##  Tecnologias e Linguagens Utilizadas

### **Linguagem Principal**
- **Java 21** 

### **Frameworks e Bibliotecas**
- **Swing** - Interface gráfica.
- **AWT** - Componentes visuais básicos.
- **Maven** - Gerenciamento de dependências.
- **SQLite** - Banco de dados local.
- **OkHttp** - Cliente HTTP para consumo de API.
- **Gson** - Processamento de JSON.


### **APIs Externas**
- **RAWG Video Games Database** - API para dados de jogos:
https://rawg.io/apidocs

### **Ferramentas de Desenvolvimento**
- **IntelliJ IDEA Ultimate** - IDE principal.
- **Git** - Controle de versão.
- **SQLite Browser** - Visualização do banco.

## Banco de Dados
### Arquivos do Sistema
* `games.db` - Banco principal SQLite.
* `database.sql` - Backup dos dados.
* `schema.sql` - Documentação da estrutura.

## Como Executar o Projeto

### Pré-requisitos
- Java 21+
- Maven 3.6+
- Internet (para API)

### Opção 1: Terminal
1. Baixe ou clone o projeto
`git clone [https://github.com/bagirodrigues/GameManager_POO]`
`cd GameManagerPOO`

2. Compile
`mvn clean install`

3. Execute
`java -jar target/GameManagerPOO-1.0-SNAPSHOT.jar`

### Opção 2: IDE (recomendado)
1. Abra o projeto no IntelliJ
2. Vá até `src/main/java/com/gamemanager/Main.java`
3. Clique no botão `▶️ "Run"`

## Configuração da API

 ### Sobre API Key: 
**Para facilitar a correção, deixei uma chave de API pré-configurada no código. Em um projeto real, essa chave seria configurada via variáveis de ambiente por questões de segurança.**

1. Acesse: https://rawg.io/apidocs;
2. Crie uma conta (gratuita);
3. Vá para "Dashboard" → "API";
4. Copie sua chave pessoal.

### Configurar no Projeto
 #### Atualizar no código
- _Em `ApiService.java`, linha 17:_
`this.apiKey = "SUA_CHAVE_AQUI"; ` _(substituir para MINHACHAVE)_
- _Também pode-se alterar em **GameService.java**, linha 16:_
`this.apiService = new ApiService("SUA_CHAVE_AQUI");` 

## Exemplos de Uso
### Adicionar jogo da API
1. Clique em "Buscar na API";
2. Digite "The Witcher 3";
3. Confirme as informações;
4. Clique em "Adicionar";
5. Pronto! Jogo está na coleção.

### Editar jogo existente
1. Clique em "Editar Jogo";
2. Digite o ID do jogo;
3. Modifique os campos;
4. Salve as alterações.

### Organizar coleção
1. Use "Atualizar" para recarregar;
2. Os jogos são ordenados por ID.


### Se Der Problema

 **"Não encontrou classe?"**

`mvn clean install  # Rode de novo`

**"API não busca?"**
* **Verifique sua internet**
* **Confira a API key**

**"Janela não abre?"**
* Verifique se tem Java 21+
* `java --version para confirmar`


_**Desenvolvimento com IA**_

_Transparência: Este projeto contou com assistência do DeepSeek para:_
* Otimização da documentação;
* Sugestões de estrutura de código;
* Revisão de boas práticas.

**Importante:** Todo o código foi desenvolvido e implementado pelo autor, utilizando a IA como ferramenta de suporte para aspectos específicos como documentação e validação de conceitos.

#### Desenvolvido para POO e Banco de Dados.

_Projeto feito por Gabriela, 2º ADS - 2025.2_ (https://github.com/bagirodrigues)
