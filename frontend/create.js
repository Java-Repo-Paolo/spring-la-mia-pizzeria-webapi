

document.getElementById('pizza-create').addEventListener('submit', function(event) {
    event.preventDefault();

    try {
        // Ottenere i dati del modulo
        var formData = new FormData(event.target);

        // Effettuare la richiesta API
        fetch('http://localhost:8080/api/v1/pizzas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: formData.get('name'),
                price: formData.get('price'),
                image: formData.get('image'),
                description: formData.get('description'),
            }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to create pizza');
            }
            return response.json();
        })
        .then(() => {
            // Dopo aver creato la pizza con successo, reindirizza alla pagina dell'index
            window.location.href = 'index.html';
        })
    } catch (error) {
        console.error('An unexpected error occurred:', error);
    }
});