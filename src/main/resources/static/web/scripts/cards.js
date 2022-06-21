
Vue.createApp({
    data() {
        return {
            datosJson: [],
            cardsJson: [],
            arrayDebito: [],
            arrayCredito: [],




        }
    },

    created() {

        this.load()


    },

    methods: {

        vencimiento() {
            let date = new Date();
            actual = date.toISOString().split('T')[0]
            console.log(actual)
            this.cardsJson.forEach(card => {
                if (card.thruDate.valueOf() < actual.valueOf()) {
                    console.log(card.thruDate + " esta vencida ")
                    axios.patch(`/api/cards/expired/${card.id}`)
                    .then(() => {
                        this.reload() 

                    } 
                    )

                }
                else {
                    console.log(card.thruDate + "no esta vencida")

                }

            })

        },

        deleteCard(id) {

            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch(`/api/cards/${id}`)
                        .then(response => {
                            Swal.fire(
                                'Deleted!',
                                'Your card has been deleted.',
                                'success'
                            )
                                .then(response => window.location.reload())
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

        load() {
            axios.get(`/api/clients/current`)
                .then(datos => {
                    this.datosJson = datos.data
                    this.cardsJson = datos.data.cards
                    this.arrayDebito = this.cardsJson.filter(card => card.type == 'DEBIT')
                    this.arrayCredito = this.cardsJson.filter(card => card.type == 'CREDIT')

                }
                ).then(() =>
                    this.vencimiento())

        },

        reload() {

            axios.get(`/api/clients/current`)
                .then(datos => {
                    this.datosJson = datos.data
                    this.cardsJson = datos.data.cards
                    this.arrayDebito = this.cardsJson.filter(card => card.type == 'DEBIT')
                    this.arrayCredito = this.cardsJson.filter(card => card.type == 'CREDIT')
                }
                )

        },


        formatearFecha(fecha) {
            //  2022-05-20
            // // fecha[0]= 2022
            // // fecha[1]=05
            // // fecha[2]=20
            fecha = fecha.split("-")
            fecha[0] = fecha[0].substring(2, 4)

            return fecha[1] + "/" + fecha[0]
        },

        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html"
                )

        },

        redirection() {
            window.location.href = "/web/create-cards.html"
        }


    }


}).mount('#app')