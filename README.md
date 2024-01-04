# TarefaGuru Android App

## Overview

TarefaGuru is a Kotlin-based Android application designed to help users organize and maintain client lists, services, and associated documents. The idea originated from the need to assist a family member in managing their clients, services, and related information.

The app employs a persistent database using the Room library and the Repository pattern to access data, ensuring a separation of concerns between the UI and the data source.

## Features

- **Client Management:**
  - Create/Edit clients with details such as Name, Phone, and Address.
  - Additional information includes the client's active status and whether they have a contract.
  - Filter clients by active status, contract status, and name.

- **Service Management:**
  - Create/Edit services associated with a client.
  - Track service details like Date, Time, Service Fee, and payment status.
  - Filter services by payment status.

- **Media and Notes:**
  - Capture and associate photos with clients for future reference.
  - Save notes and observations related to clients.

- **Dashboard:**
  - View the number of pending services.
  - Track the total amount to be received.
  - Get a quick overview of the next upcoming service.

- **Filtering:**
  - List clients based on their active status, contract status, and name.
  - Filter services by payment status.

- **Integration with Android Services:**
  - Call clients directly from the application using Android services.
  - Utilize a navigation application to show the client's location on the map.

## Screenshots

![Dashboard](https://github.com/johulk/TarefaGuru/assets/2285304/0eef8b92-1fcf-4db3-b268-b95ed61ade69)

![Client Details](https://github.com/johulk/TarefaGuru/assets/2285304/05f73bcb-6882-4314-9fc8-ce7eb913256e)
