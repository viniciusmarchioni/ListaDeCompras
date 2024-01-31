Descrição:
A classe MainActivity é a atividade principal da aplicação que gerencia a entrada do usuário, criação de sessões e navegação entre as telas. Ela utiliza o Firebase Database para verificar a existência de uma chave (código) e navega para a SessionActivity se a chave existir.

Métodos Principais:
onCreate: Método responsável por inicializar a atividade, definir os elementos da interface do usuário e configurar os ouvintes de clique.

timer: Função assincrona que implementa um temporizador para remoção de um ouvinte de eventos do Firebase após 5 segundos, fiz devido a falta de existencia de um timeout na documentação do firebase.

generateCode: Método que gera um código aleatório de 10 caracteres para uma nova sessão.

Variáveis Principais:
databaseReference: Referência ao Firebase Database.
response: Variável booleana para rastrear a resposta da verificação da existência da chave.
Principais Bibliotecas Utilizadas:
Firebase Database para comunicação com o banco de dados em tempo real.
Daimajia Android Library para animações.
Kotlin Coroutines para execução assíncrona.

A classe SessionActivity gerencia a sessão ativa, permitindo a adição e remoção de produtos. Ela utiliza o Firebase Database para salvar e recuperar os produtos associados a uma sessão.

Métodos Principais:
onCreate: Método responsável por inicializar a atividade, configurar a interface do usuário e definir ouvintes de eventos.

refreshList: Atualiza a lista de produtos com base nos dados do Firebase Database para uma sessão específica.

saveProduct: Salva um produto no Firebase Database associado a uma sessão.

Variáveis Principais:
listaProdutos: Lista de produtos associados à sessão.
db: Referência ao Firebase Database.
binding: Objeto da classe ActivitySessionBinding para vincular elementos da interface do usuário.
Principais Bibliotecas Utilizadas:
Firebase Database para comunicação com o banco de dados em tempo real.
RecyclerView para exibir a lista de produtos.
MediaPlayer para reprodução de som de clique.
SwipeLeftRightCallback para manipulação de gestos de deslize em um RecyclerView.

Os respectivos xmls podem ser encontrados na pasta Layouts
Assets de visuais na pasta drawable
Assets de sonoros na pasta raw 
Traduções e estilizações em values





