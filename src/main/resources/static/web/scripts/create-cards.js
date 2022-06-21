
Vue.createApp({
    data() {
        return {
            datosJson: [],
            selectedCardType: "",
            selectedCardColor: "",
 



        }
    },

    created() {

        axios.get(`http://localhost:8080/api/clients/current`)
            .then(datos => {
                this.datosJson = datos.data

                document.querySelector("#loader").classList.toggle("loader2")

            }
            )

    },

    methods: {

        create() {

            Swal.fire({
                title: "Confirmation",
                text: "Are you sure you want to apply for the card?",
                icon: "warning",
                showCancelButton: true,
                cancelButtonText: 'No, cancel!',
                confirmButtonText: 'Yes, Request!',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/clients/current/cards',`cardType=${this.selectedCardType}&cardColor=${this.selectedCardColor}`,
                    {headers:{'content-type': 'application/x-www-form-urlencoded'}})
                    .then(response => {
                            Swal.fire({
                                title: "Congratulations!",
                                text: "The card was created.",
                                icon: "success",
                                button: "Ok"
                            })
                            .then(response =>  window.location.href = "http://localhost:8080/web/cards.html")
                            
                        })
                        .catch(error => {
                            Swal.fire({
                                text: error.response.data,
                                icon: "error"
                            })
                        })
                }
            })


            


        },

        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "http://localhost:8080/web/index.html"
                )

        },


    }


}).mount('#app')