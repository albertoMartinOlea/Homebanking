Vue.createApp({
    data() {
        return {
            datosJson: [],
            datosJsonAccounts: [],
            transactions: [],
            id: "",
            since: "",
            till: ""

        }
    },

    created() {
        const urlParams = new URLSearchParams(window.location.search);
        this.id = urlParams.get('id');
        axios.get(`/api/clients/current`)
            .then(datos => {

                // console.log(datos)
                this.datosJson = datos.data


            }
            )

        axios.get(`/api/accounts/${this.id}`)
            .then(datosT => {

                this.datosJsonAccounts = datosT.data
                this.transactions = datosT.data.transactions
                this.transactions.sort((a, b) => {
                    if (a.id > b.id) {
                        return -1;
                    } else if (a.id < b.id) {
                        return 1;
                    } else {
                        return 0;
                    }
                })


            }
            )

    },

    methods: {
        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html"
                )

        },

        printTransactions() {

            Swal.fire({
                title: 'Quiere guardar este archivo Pdf?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: 'Guardar',
                denyButtonText: 'No guardar',
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire('Guardado!', '', 'success')
                    axios.post(`/api/pdf/generate/${this.id}`, `desde=${this.since}&hasta=${this.till}`,{ 'responseType': 'blob' })
                        .then(response => {
                            let url = window.URL.createObjectURL(new Blob([response.data]))
                            let link = document.createElement("a")
                            link.href = url;
                            link.setAttribute("download",`${"transactions"}_${ this.since}-${this.till}.pdf`)
                            document.body.appendChild(link)
                            link.click()
                        })
                } else if (result.isDenied) {
                    Swal.fire('no se guardo', '', 'info')
                }
            })

        }


    }


}).mount('#app')