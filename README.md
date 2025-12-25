# 🏦 Ma Banque - Système de Gestion Bancaire / Application-full-stack-REST


Ce projet est une application web de gestion bancaire complète reposant sur une architecture Client-Serveur. Il permet de piloter des comptes clients, d'effectuer des virements sécurisés et d'assurer la persistance des données via une base de données MySQL.

---

## 📸 Aperçu des Fonctionnalités

### 🖥️ Tableau de Bord Principal
L'interface utilisateur permet de visualiser la **Liste des Comptes** en temps réel. On y retrouve les noms des clients comme **Ali, Sami, kaouther et dhia** avec leurs soldes respectifs.

<img width="1039" height="561" alt="Dashboard Ma Banque" src="https://github.com/user-attachments/assets/81e4a683-6c2b-4be0-bf42-b412472ffeaa" />

### 💸 Gestion des Virements par ID
Le système intègre un module permettant de transférer des fonds entre comptes de manière précise en utilisant les identifiants (ID) du compte débiteur et créditeur.
* **Test de validation** : Un virement de **500.0 €** a été réalisé avec succès.
* **Confirmation** : Un message de succès s'affiche après l'opération : "Virement de 500.0 effectué".

<img width="445" height="453" alt="Formulaire de virement" src="https://github.com/user-attachments/assets/696b81ec-5b24-4af9-8efd-0c297032a09d" />
<img width="770" height="353" alt="Confirmation de virement" src="https://github.com/user-attachments/assets/c7ead7d2-8e0c-4c9a-96b4-96f06e162bf5" />

### ➕ Création de Nouveaux Comptes
L'application offre un formulaire dédié pour l'ajout de nouveaux clients dans le système en définissant un nom et un solde initial.

<img width="1038" height="397" alt="Création de compte" src="https://github.com/user-attachments/assets/34e04471-cf91-4a11-8374-a6c057c0f251" />

---

## 🗄️ Base de Données

Les informations sont stockées dans une base de données MySQL nommée `compte`.
* **Table principale** : `comptes`.
* **Attributs visibles** : `id`, `nom`, `solde`.

<img width="877" height="497" alt="Structure phpMyAdmin" src="https://github.com/user-attachments/assets/b9d48dfe-8e29-43fa-9645-b2f1a23c54f4" />

---

## 🛠️ Stack Technique

* **Architecture** : Client-Serveur (Back + Front).
* **Backend** : Java (Projet `AppServer`).
* **Frontend** : JSP (Projet `AppClient-jsp`).
* **Serveur local** : Exécution sur `localhost:8888`.
* **Base de Données** : MySQL administré via **phpMyAdmin**.

---

## 📂 Organisation du Code Source

Le projet est organisé en deux dossiers principaux pour séparer la logique serveur de l'interface client :
1. **AppServer** : Logique métier et serveur.
2. **AppClient-jsp** : Interface utilisateur et pages JSP.
