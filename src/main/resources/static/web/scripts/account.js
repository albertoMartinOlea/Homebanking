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
        axios.get(`http://localhost:8080/api/clients/current`)
            .then(datos => {

                // console.log(datos)
                this.datosJson = datos.data


            }
            )

        axios.get(`http://localhost:8080/api/accounts/${this.id}`)
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
                .then(response => window.location.href = "http://localhost:8080/web/index.html"
                )

        },

        printTransactions(){
            axios.post(`http://localhost:8080/api/pdf/${this.id}`,`desde=${this.since}&hasta=${this.till}`)
            .then(()=>console.log("descargado"))

        }


    }


}).mount('#app')