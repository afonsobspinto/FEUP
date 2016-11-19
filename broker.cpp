#include <fstream>

#include "broker.h"
#include "interacao.h"
#include "menu.h"
#include "utils.h"

using namespace std;

Broker::Broker(std::string nome) {
	this->nome = nome;
	ficheiroClientes = nome + "_clientes.txt";
	ficheiroFornecedores = nome + "_fornecedores.txt";
	receita = 0;

	ofstream ficheiro(nome+".txt");

	ficheiro << nome << endl;
	ficheiro << ficheiroClientes << endl;
	ficheiro << ficheiroFornecedores << endl;
	ficheiro << receita << endl;

	ficheiro.close();
	guardaClientes();
	guardaFornecedores();

}

Broker::Broker(std::string nome, std::string ficheiroClientes,
		std::string ficheiroFornecedores, float receita) {
	this->nome = nome;
	this->receita = receita;
	this->ficheiroClientes = ficheiroClientes;
	this->ficheiroFornecedores = ficheiroFornecedores;
	clientes = leFicheiroClientes();
	fornecedores = leFicheiroFornecedores();
	atualizaMontra();
}

vector<Registado> Broker::getClientes() const {
	return clientes;
}

vector<Imovel*> Broker::getMontra() const {
	return montra;
}

vector<Fornecedor> Broker::getFornecedores() const {
	return fornecedores;
}

float Broker::getReceita() const {
	return receita;
}

bool Broker::adicionaCliente() {
	unsigned int size = getClientes().size();
	Registado C = criaCliente();

	if(C.getNome()=="" || C.getPassword()=="")
		return false;

	for (unsigned int i = 0; i < size; i++){
		try{
			if(C.getNome() == getClientes().at(i).getNome()){
					throw UtilizadorJaExistente(C.getNome());
				}
			}
			catch (UtilizadorJaExistente &e) {
				cout << "Apanhou excecao." << e.getNome() << " já foi utilizado. \n";
				return false;
			}
	}

	clientes.push_back(C);
	guardaClientes();
	return true;
}

bool Broker::adicionaFornecedor() {
	Fornecedor F = criaFornecedor();

	if (F.getNome() == "" || F.getNif()==0 || F.getMorada() == "")
		return false;

	unsigned int size = getFornecedores().size();

	for (unsigned int i = 0; i < size; i++){
		try{
					if(F.getNif() == getFornecedores().at(i).getNif()){
							throw FornecedorJaExistente(F.getNif());
						}
					}
					catch (FornecedorJaExistente &e) {
						cout << "Apanhou excecao." << e.getNif() << " já foi utilizado. \n";
						return false;
					}
	}

	fornecedores.push_back(F);
	atualizaMontra();
	guardaFornecedores();
}

bool Broker::adicionaImovel() {
/*	Imovel *I = criaImovel();

	montra.push_back(I);
	atualizaMontra()
	guardaFornecedores();
	*/
}

bool Broker::atualizaMontra() {
	int size = fornecedores.size();
	vector<Imovel *> m;

	for (unsigned int i=0; i<size; i++){
		int fsize = fornecedores.at(i).getOfertas().size();
		for (unsigned int j=0; j<fsize; j++){
			m.push_back(fornecedores.at(i).getOfertas().at(j));
		}
	}
		montra = m;
	return true;

	// Adicionar return false em caso de erro
}

bool Broker::efectuaReserva(Cliente C, Imovel I, Data D1, Data D2) {
/*
	unsigned int size = I.getReservas().size();
	unsigned int c = 0;

	for (unsigned int i=0; i< size; i++){
		Data di = I.getReservas().at(i).getInicio();
		Data df = I.getReservas().at(i).getFinal();
		if (dias_sobrepostos(di,df,D1,D2)==0){
			return false;
		}
		else{
			return true;
			C.getPontos()++;
	}*/
}

void Broker::taxa() {
	int size = montra.size();

	for (unsigned int i=0; i < size; i++){
		receita += montra.at(i)->getTaxa();
	}
}

bool Broker::cancelaReserva(Data& atual) {

}


bool Broker::validaLogin(std::string nome, std::string password) {
}


std::vector<Fornecedor> Broker::leFicheiroFornecedores() {

	vector<Fornecedor>fornecedores;
	string linha;
	ifstream ficheiro(ficheiroFornecedores);
	unsigned int size;

	getline(ficheiro, linha);
	size = stoi(linha);


	cout << size << "<-size" << endl;

	string nome;
	int nif;
	string morada;

	string nif_str;

	string localidade;
	int owner;
	float preco;
	float taxa;
	string tipo;

	string preco_str;
	string taxa_str;

	string reservas;

	Data dataInicio;
	Data dataFim;

	string dataInicio_str;
	string dataFim_str;


	for (unsigned int i=0; i < size; i++){

		getline(ficheiro, nome, ';');
		getline(ficheiro, nif_str, ';');
		getline(ficheiro, morada, ';');

		nome = nome.substr(0, nome.length()-1);
		nif = stoi(nif_str);
		owner = nif;
		morada = morada.substr(1, morada.length()-2);

		cout << nome <<"<-nome" << endl;
		cout << nif <<"<-nif" << endl;
		cout << morada <<"<-morada" << endl;

		unsigned int ofertas;

		getline(ficheiro, linha, ';');
		ofertas = stoi(linha);


		cout << ofertas <<"<-size_ofertas" << endl;

		vector <Imovel*> imoveis;
		for (unsigned int j=0; j < ofertas; j++){


			getline(ficheiro, localidade, ';');
			getline(ficheiro, tipo, ';');
			getline(ficheiro, preco_str, ';');
			getline(ficheiro, taxa_str, ';');

			localidade = localidade.substr(1,localidade.length()-2);
			tipo = tipo.substr(1,tipo.length()-2);
			preco = stof(preco_str);
			taxa = stof(taxa_str);



			cout << localidade <<"<-localidade" << endl;
			cout << tipo <<"<-tipo" << endl;
			cout << preco <<"<-preco" << endl;
			cout << taxa <<"<-taxa" << endl;

			unsigned int reservas_size;

			getline(ficheiro, linha, ';');
			reservas_size = stoi(linha);

			cout << reservas_size <<"<-reservas_size" << endl;

			vector <Reserva> reservas;

			bool suite;
			bool cozinha;
			bool sala_de_estar;
			int cama;

			string suite_str;
			string cozinha_str;
			string sala_de_estar_str;
			string cama_str;

			bool cama_extra;

			string cama_extra_str;

			for (unsigned int k=0; k < reservas_size; k++){

				getline(ficheiro, dataInicio_str, ';');
				getline(ficheiro, dataFim_str, ';');

				dataInicio = string2data(dataInicio_str.substr(1, dataInicio_str.length()));
				dataFim = string2data(dataFim_str.substr(1, dataFim_str.length()));

				Reserva R(dataInicio, dataFim);
				reservas.push_back(R);

				if(tipo == "Apartamento"){

					getline(ficheiro, suite_str, ';');
					getline(ficheiro, cozinha_str, ';');
					getline(ficheiro, sala_de_estar_str, ';');
					getline(ficheiro, cama_str, ';');

					suite = stoi(suite_str);
					cozinha = stoi(cozinha_str);
					sala_de_estar = stoi(sala_de_estar_str);
					cama = stoi(cama_str);

					cout << suite << endl;
					cout << cozinha << endl;
					cout << sala_de_estar << endl;
					cout << cama << endl;

				}

				else if (tipo == "Hotel"){


					getline(ficheiro, cama_str, ';');
					getline(ficheiro, cama_extra_str, ';');


					cama = stoi(cama_str.substr(1,cama_str.length()));
					cama_extra = stoi(cama_extra_str.substr(1,cama_extra_str.length()));

					cout << cama << endl;
					cout << cama_extra << endl;

				}
			}
			if(tipo == "Apartamento"){
				Imovel *I = new Apartamento(localidade, owner, preco, reservas, cama, suite, cozinha, sala_de_estar);
				imoveis.push_back(I);
			}

			else if (tipo == "Hotel"){
				Imovel *I = new Hotel(localidade, owner, preco, reservas, cama, cama_extra);
				imoveis.push_back(I);
			}

			else if(tipo=="Flat"){
				Imovel *I = new Flat(localidade, owner, preco, reservas);
				imoveis.push_back(I);
			}

			else if(tipo=="BB"){
				Imovel *I = new BB(localidade, owner, preco, reservas);
				imoveis.push_back(I);
			}

			else if(tipo=="Shared"){
				Imovel *I = new Shared(localidade, owner, preco, reservas);
				imoveis.push_back(I);
			}

			else{
				cout << "erro na leitura" << endl; //Criar uma exceçao
			}
		}


		Fornecedor F(nome, nif, morada, imoveis);

		fornecedores.push_back(F);
	}

	ficheiro.close();
	return fornecedores;
}


std::vector<Registado> Broker::leFicheiroClientes() {

	vector<Registado>clientes;
	string linha;
	ifstream ficheiro(ficheiroClientes);
	unsigned int size;

	cout << endl << "A ler ficheiro clientes" << endl << endl;

	getline(ficheiro, linha);

	cout << linha << endl;

	size = stoi(linha);

	string nome;
	int pontos;
	float valor;
	string password;
	string pontos_str;
	string valor_str;


	for (unsigned int i=0; i < size; i++){

		getline(ficheiro, nome, ';');
		getline(ficheiro, pontos_str, ';');
		getline(ficheiro, valor_str, ';');
		getline(ficheiro, password);

		pontos = stoi(pontos_str);
		valor = stof(valor_str);
		password = password.substr(1,password.length());

		cout << nome << endl;
		cout << pontos << endl;
		cout << valor << endl;
		cout << password << endl;

		Registado C(nome, pontos, valor, password);

		clientes.push_back(C);
	}

	ficheiro.close();

	return clientes;
}

void Broker::guardaClientes() {

	ofstream ficheiro(ficheiroClientes, ios::trunc);

	ficheiro << clientes.size() << endl;

	for (size_t i = 0; i < clientes.size(); i++)
	{
		ficheiro << clientes.at(i).getNome()<< " ; " << clientes.at(i).getPontos() << " ; " << clientes.at(i).getValor() << " ; " << clientes.at(i).getPassword() << endl;
	}

	ficheiro.flush();

	cout << "Clientes Guardados com Sucesso! " << endl ;
}

void Broker::guardaFornecedores() {

	ofstream ficheiro(ficheiroFornecedores, ios::trunc);

	ficheiro << fornecedores.size() << endl;

	for (unsigned int  i = 0; i < fornecedores.size(); i++)
	{
		ficheiro << fornecedores.at(i).getNome()<< " ; " << fornecedores.at(i).getNif() << " ; " << fornecedores.at(i).getMorada()<< " ; " <<
				fornecedores.at(i).getOfertas().size() << " ; ";
		for (unsigned int j = 0; j < fornecedores.at(i).getOfertas().size(); j++){
			ficheiro << fornecedores.at(i).getOfertas().at(j)->getLocalidade() << " ; " << fornecedores.at(i).getOfertas().at(j)->getTipo() << " ; " << fornecedores.at(i).getOfertas().at(j)->getPreco() << " ; " << fornecedores.at(i).getOfertas().at(j)->getTaxa() << " ; " <<
					fornecedores.at(i).getOfertas().at(j)->getReservas().size() << " ; ";
			for (unsigned int k = 0; k < fornecedores.at(i).getOfertas().at(j)->getReservas().size(); k++){
				ficheiro << data2string(fornecedores.at(i).getOfertas().at(j)->getReservas().at(k).getInicio()) << " ; " <<
						data2string(fornecedores.at(i).getOfertas().at(j)->getReservas().at(k).getInicio()) << " ; ";

				if(fornecedores.at(i).getOfertas().at(j)->getTipo() == "Apartamento"){
					ficheiro << fornecedores.at(i).getOfertas().at(j)->getSuite() << " ; " <<
							fornecedores.at(i).getOfertas().at(j)->getCozinha() << " ; " <<
							fornecedores.at(i).getOfertas().at(j)->getSala_de_estar() << " ; " <<
							fornecedores.at(i).getOfertas().at(j)->getCama() << " ; " << " ; ";
				}
				else if(fornecedores.at(i).getOfertas().at(j)->getTipo() == "Hotel"){
					ficheiro << fornecedores.at(i).getOfertas().at(j)->getCama() << " ; " <<
							fornecedores.at(i).getOfertas().at(j)->getCama_extra() << " ; ";
				}
			}
		}
	}

	ficheiro.flush();

	cout << "Fornecedores Guardados com Sucesso! " << endl ;
}

void Broker::mostraMontra() {
	unsigned int size = montra.size();

	for (unsigned int i=0; i < size; i++){
		cout << "Tipo: " << montra.at(i)->getTipo() << endl;
		cout << "Localidade: " << montra.at(i)->getLocalidade() << endl;
		cout << "Preço: " << montra.at(i)->getPreco() << endl;
		cout << endl;
	}
}

void Broker::mostraMontra(std::string localidade) {
	unsigned int size = montra.size();

	vector <Imovel*> vec = montra;

	vec = ordenaMontra(vec, false);

	for (unsigned int i=0; i < size; i++){
		if(vec.at(i)->getLocalidade()==localidade){
			cout << "Tipo: " << vec.at(i)->getTipo() << endl;
			cout << "Localidade: " << vec.at(i)->getLocalidade() << endl;
			cout << "Preço: " << vec.at(i)->getPreco() << endl;
			cout << endl;
		}
	}
}

void Broker::mostraMontra(std::string localidade, Data inicio, Data fim) {
	unsigned int size = montra.size();

	vector <Imovel*> vec = montra;

	vec = ordenaMontra(vec, false);

	for (unsigned int i=0; i< size; i++){
		if (vec.at(i)->getLocalidade()==localidade){
			unsigned int rsize = vec.at(i)->getReservas().size();
			for (unsigned int j=0; j<rsize; j++){
				Data di = vec.at(i)->getReservas().at(j).getInicio();
				Data df = vec.at(i)->getReservas().at(j).getFinal();
				if (dias_sobrepostos(di,df,inicio,fim)){
					cout << "Tipo: " << vec.at(i)->getTipo() << endl;
					cout << "Localidade: " << vec.at(i)->getLocalidade() << endl;
					cout << "Preço: " << vec.at(i)->getPreco() << endl;
					cout << endl;
				}
			}
		}
	}
}

void Broker::mostraMontra(float preco) {
	unsigned int size = montra.size();

	vector <Imovel*> vec = montra;

	vec = ordenaMontra(vec, false);

	for (unsigned int i=0; i< size; i++){
		if (vec.at(i)->getPreco() <= preco){
			cout << "Tipo: " << vec.at(i)->getTipo() << endl;
			cout << "Localidade: " << vec.at(i)->getLocalidade() << endl;
			cout << "Preço: " << vec.at(i)->getPreco() << endl;
			cout << endl;
		}
	}
}

void Broker::mostraMontra(float preco, Data inicio, Data fim) {
	unsigned int size = montra.size();

	vector <Imovel*> vec = montra;

	vec = ordenaMontra(vec, false);

	for (unsigned int i=0; i< size; i++){
		if (vec.at(i)->getPreco()<=preco){
			unsigned int rsize = vec.at(i)->getReservas().size();
			for (unsigned int j=0; j<rsize; j++){
				Data di = vec.at(i)->getReservas().at(j).getInicio();
				Data df = vec.at(i)->getReservas().at(j).getFinal();
				if (dias_sobrepostos(di,df,inicio,fim)){
					cout << "Tipo: " << vec.at(i)->getTipo() << endl;
					cout << "Localidade: " << vec.at(i)->getLocalidade() << endl;
					cout << "Preço: " << vec.at(i)->getPreco() << endl;
					cout << endl;
				}
			}
		}
	}
}

void Broker::mostraMontra(std::string localidade, float preco, Data inicio, Data fim) {
	unsigned int size = montra.size();

	vector <Imovel*> vec = montra;

	vec = ordenaMontra(vec, false);

	for (unsigned int i=0; i< size; i++){
		if (vec.at(i)->getPreco()<=preco && vec.at(i)->getLocalidade()==localidade){
			unsigned int rsize = vec.at(i)->getReservas().size();
			for (unsigned int j=0; j<rsize; j++){
				Data di = vec.at(i)->getReservas().at(j).getInicio();
				Data df = vec.at(i)->getReservas().at(j).getFinal();
				if (dias_sobrepostos(di,df,inicio,fim)){
					cout << "Tipo: " << vec.at(i)->getTipo() << endl;
					cout << "Localidade: " << vec.at(i)->getLocalidade() << endl;
					cout << "Preço: " << vec.at(i)->getPreco() << endl;
					cout << endl;
				}
			}
		}
	}
}
