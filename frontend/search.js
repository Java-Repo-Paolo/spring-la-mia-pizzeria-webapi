document.addEventListener('DOMContentLoaded', function () {
    const searchButton = document.getElementById('searchButton');
    searchButton.addEventListener('click', searchPizzas);
});

function searchPizzas() {
    const searchPizza = document.getElementById('searchBar').value.toLowerCase();

    console.log('Search button clicked. Searching for:', searchPizza);

    fetch(`http://localhost:8080/api/v1/pizzas`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch pizzas');
            }
            return response.json();
        })
        .then(data => {
            // Filtra le pizze in base al nome
            const filteredPizzas = data.filter(pizza => pizza.name.toLowerCase().includes(searchPizza));
            console.log('Filtered pizzas:', filteredPizzas);
            updatePizzaList(filteredPizzas);
        })
        .catch(error => {
            console.error('An unexpected error occurred:', error);
        });
}

function updatePizzaList(data) {
    const root = document.getElementById('root');
    
    if (root) {
        let content = '<div class="row">';
        data.forEach((element) => {
            content += '<div class="col-3 mb-4">';
            content += cardPizza(element);
            content += '</div>';
        });
        content += '</div>';
        root.innerHTML = content;
    } else {
        console.error('Root element not found');
    }
}