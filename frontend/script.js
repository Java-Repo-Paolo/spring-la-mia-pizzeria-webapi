const apiUrl = 'http://localhost:8080/api/v1/pizzas';
const root = document.getElementById('root');
// funzione che chiama l'api



//Inizializzo la card

const cardPizza = (element) =>{
    console.log(element);
    return `<div class="card h-100 bg-dark text-light border-2 border-secondary" style="width: 22 rem;">
                <img src=${element.image} class="card-img-top h-100" alt="...">
                <div class="card-body">
                    <h5 class="card-title">${element.name}</h5>
                    <p class="card-text">${element.description} </p>
                </div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item bg-dark text-light border-secondary">
                       <span>Price: ${element.price} €</span>
                    </li>
                    <li class="list-group-item bg-dark">
                        <a href="#" class="card-link">Card link</a>
                        <a href="#" class="card-link">Another link</a>
                    </li>
                </ul>
                <div class="card-body">
                   
                    <button class="btn btn-danger" onclick="deletePizza($   {element.id})">
                        Delete Pizza
                    </button>
                    
                </div>
            </div>`;
}

//Inizializzo la lista delle card
const pizzaList = (data) => {
    let content;
    console.log(data);
    // condizioni
    if (data.length > 0) {
        content = '<div class="row">';

        data.forEach((element) => {
            content += '<div class="col-3 mb-4">';
            content += cardPizza(element);
            content += '</div>';
        });
    } else {
        content = '<div class="alert alert-info">The list is empty</div>';
    }
     // sostituisco il contenuto di root con il mio content
    root.innerHTML = content;
}

// funzione che chiama l'api e ottiene il json con l'array di pizzas

const getPizza = async () => {
    try{
        const response = await axios.get(apiUrl);
        pizzaList(response.data);
    } catch (error) {
        console.log(error);
    }
}



// Funzione per eliminare la pizza
const deletePizza = async (pizzaId) => {
    try {
        const response = await fetch(`${apiUrl}/${pizzaId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                // Aggiungi eventuali altri header necessari
            },
        });

        if (!response.ok) {
            throw new Error(`Errore durante la cancellazione della pizza con ID ${pizzaId}`);
        }

        console.log(`Pizza con ID ${pizzaId} eliminata con successo`);
        // Puoi aggiornare la visualizzazione o eseguire altre azioni dopo la cancellazione
        location.reload();
    } catch (error) {
        console.error(error.message);
        // Gestisci l'errore in base alle tue esigenze
    }
};

getPizza();


