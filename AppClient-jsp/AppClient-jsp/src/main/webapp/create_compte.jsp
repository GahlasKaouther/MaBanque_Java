<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Créer un Compte - Ma Banque</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .card { box-shadow: 0 4px 6px rgba(0,0,0,0.1); border: none; }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header bg-success text-white">
                    <h4 class="mb-0">➕ Créer un Nouveau Compte</h4>
                </div>
                <div class="card-body">
                    <form action="comptes" method="post">
                        <!-- Action cachée pour dire à la Servlet que c'est une création -->
                        <input type="hidden" name="action" value="create">

                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom du Client</label>
                            <input type="text" class="form-control" id="nom" name="nom" required placeholder="Ex: Jean Dupont">
                        </div>

                        <div class="mb-4">
                            <label for="solde" class="form-label">Solde Initial (€)</label>
                            <div class="input-group">
                                <input type="number" step="0.01" class="form-control" id="solde" name="solde" required value="0.00">
                                <span class="input-group-text">€</span>
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-success btn-lg">Créer le Compte</button>
                            <a href="comptes" class="btn btn-outline-secondary">Annuler</a>
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
