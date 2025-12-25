<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ma Banque - Gestion des Comptes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .card { box-shadow: 0 4px 6px rgba(0,0,0,0.1); border: none; }
        .header { background: linear-gradient(135deg, #0d6efd, #0a58ca); color: white; padding: 2rem 0; margin-bottom: 2rem; }
    </style>
</head>
<body>

<div class="header text-center">
    <h1>🏦 Ma Banque</h1>
    <p class="lead">Gestion simple et rapide de vos comptes</p>
</div>

<div class="container">
    
    <!-- Messages d'erreur ou de succès -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row">
        <!-- Liste des comptes -->
        <div class="col-md-8">
            <div class="card mb-4">
                <div class="card-header bg-white d-flex justify-content-between align-items-center">
                    <h4 class="mb-0 text-primary">📋 Liste des Comptes</h4>
                    <a href="create_compte.jsp" class="btn btn-success btn-sm">➕ Nouveau Compte</a>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty comptes}">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th>ID</th>
                                            <th>Nom du Client</th>
                                            <th class="text-end">Solde</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="compte" items="${comptes}">
                                            <tr>
                                                <td><span class="badge bg-secondary">#${compte.id}</span></td>
                                                <td><strong>${compte.nom}</strong></td>
                                                <td class="text-end font-monospace fs-5 
                                                    ${compte.solde < 0 ? 'text-danger' : 'text-success'}">
                                                    ${compte.solde} €
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-4 text-muted">
                                <p>Aucun compte trouvé ou impossible de joindre le serveur.</p>
                                <a href="comptes" class="btn btn-outline-primary btn-sm">🔄 Actualiser</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Formulaire de virement -->
        <div class="col-md-4">
            <div class="card">
                <div class="card-header bg-white">
                    <h4 class="mb-0 text-primary">💸 Effectuer un Virement</h4>
                </div>
                <div class="card-body">
                    <form action="comptes" method="post">
                        <div class="mb-3">
                            <label for="compteA" class="form-label">Compte Débiteur (ID)</label>
                            <input type="number" class="form-control" id="compteA" name="compteA" required placeholder="Ex: 1">
                        </div>
                        <div class="mb-3">
                            <label for="compteB" class="form-label">Compte Créditeur (ID)</label>
                            <input type="number" class="form-control" id="compteB" name="compteB" required placeholder="Ex: 2">
                        </div>
                        <div class="mb-3">
                            <label for="montant" class="form-label">Montant (€)</label>
                            <div class="input-group">
                                <input type="number" step="0.01" class="form-control" id="montant" name="montant" required placeholder="0.00">
                                <span class="input-group-text">€</span>
                            </div>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">Valider le Virement</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>