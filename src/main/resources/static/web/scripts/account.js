Vue.createApp({
    data() {
        return {
            datosJson:[],
            datosJsonAccounts: [],
            transactions: [],
            id:"",
            since:"",
            till:""

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

        printTransactions(){
            axios.post(`/api/pdf/${this.id}`,`desde=${this.since}&hasta=${this.till}`)
            .then(()=>console.log("descargado"))

        }


    }


}).mount('#app')