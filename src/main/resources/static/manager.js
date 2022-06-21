Vue.createApp({
  data() {
    return {
      message: 'Hello Vue!',
      datosJsonClientes: [],
      clientes: [],
      newFirstName: "",
      newLastName: "",
      newEmail: "",
      editFirstName: "",
      editLastName: "",
      editEmail: "",

      loanName: "",
      maxAmount: "",
      payments: "",
      interest: "",



    }
  },

  created() {
    axios.get(`http://localhost:8080/rest/clients`)
      .then(datos => {
        this.datosJsonClientes = datos.data
        this.clientes = datos.data._embedded.clients

      }
      )

  },

  methods: {

    addClient() {
      let client = {
        firstName: this.newFirstName,
        lastName: this.newLastName,
        email: this.newEmail
      }

      axios.post(`http://localhost:8080/rest/clients`, client)
      //console.log(this.clientes)
      // console.log(client)
    },

    deleteClient(unCliente) {

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
          axios.delete(unCliente._links.client.href)
          Swal.fire(
            'Deleted!',
            'The client has been deleted.',
            'success'

          ).then(() => location.reload())

        }

      })
      // console.log(unCliente._links.client.href)
    },

    editClient(unCliente) {

      let client = {
        firstName: this.editFirstName,
        lastName: this.editLastName,
        email: this.editEmail
      }
      axios.put(unCliente._links.client.href, client)
      location.reload()




    },

    createTypeLoan() {


      let arrayPayments=[]
      
      switch (this.payments) {
        case 1:
          arrayPayments = [6, 12]
          break;
        case 2:
          arrayPayments = [6, 12, 24]
          break;
        case 3:
          arrayPayments = [6, 12, 24, 36]
          break;
        case 4:
          arrayPayments = [12, 24]
          break;
        case 5:
          arrayPayments = [12, 24, 36]
          break;
        case 6:
          arrayPayments = [12, 24, 36, 48]
          break;
        default:
          console.log('Elija una opcion valida');
      }

      Swal.fire({
        title: "Confirmation",
        text: "Are you sure to make Are you sure you want to create a new type of loan?",
        icon: "warning",
        showCancelButton: true,
        cancelButtonText: 'No, cancel!',
        confirmButtonText: 'Yes, Request!',
      }).then((result) => {
        if (result.isConfirmed) {
          axios.post('/api/createLoans', { name: this.loanName, maxAmount: this.maxAmount, payments: arrayPayments, interest: this.interest })
            .then(response => {
              Swal.fire({
                title: "Confirmed!",
                text: "The type of loan was created.",
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

    }



  }


}).mount('#app')