
Vue.createApp({
    data() {
        return {
            datosJson: [],
            ownAccounts: [],
            selectedAmount: "",
            selectedDescription: "",
            accountType: "",
            numberAccountDestiny: "",
            numberAccountOrigin: "",




        }
    },

    created() {

        axios.get(`/api/clients/current`)
            .then(datos => {
                this.datosJson = datos.data
                this.ownAccounts = datos.data.accounts
                document.querySelector("#loader").classList.toggle("loader2")
            }
            )

    },

    methods: {

        createTransfer() {

            Swal.fire({
                title: "Confirmation",
                text: "Are you sure to make the transfer?",
                icon: "warning",
                showCancelButton: true,
                cancelButtonText: 'No, cancel!',
                confirmButtonText: 'Yes, transfer!',
                // buttons: [true, "Request"]
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/transactions', `amount=${this.selectedAmount}&description=${this.selectedDescription}&numberAccountOrigin=${this.numberAccountOrigin}&numberAccountDestiny=${this.numberAccountDestiny}`,
                        { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(response => {
                            Swal.fire({
                                title: "Confirmed!",
                                text: "Transfer made successfully.",
                                icon: "success",
                                button: "Ok"
                            })
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

        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html"
                )

        },
        filterOwnAccounts() {
            let aux = [...this.ownAccounts]
            return aux.filter((account) => account.number != this.numberAccountOrigin);
        },


    }


}).mount('#app')