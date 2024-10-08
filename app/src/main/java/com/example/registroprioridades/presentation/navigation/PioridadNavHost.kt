package com.example.registroprioridades.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.registroprioridades.presentation.cliente.ClienteListScreen
import com.example.registroprioridades.presentation.cliente.ClienteScreen
import com.example.registroprioridades.presentation.home.HomeScreen
import com.example.registroprioridades.presentation.prioridad.PrioridadListScreen
import com.example.registroprioridades.presentation.prioridad.PrioridadScreen
import com.example.registroprioridades.presentation.sistema.SistemaListScreen
import com.example.registroprioridades.presentation.sistema.SistemaScreen
import com.example.registroprioridades.presentation.ticket.TicketListScreen
import com.example.registroprioridades.presentation.ticket.TicketScreen

@Composable
fun PrioridadNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeScreen
    ) {
        composable<Screen.HomeScreen>  {
            HomeScreen(navController = navHostController)
        }
        composable<Screen.SistemaList> {
            SistemaListScreen(
                createSistema = {
                    navHostController.navigate(Screen.Sistema(0, false))
                },
                onEditSistema = {
                    navHostController.navigate(Screen.Sistema(it, false))
                },
                onDeleteSistema = {
                    navHostController.navigate(Screen.Sistema(it, true))
                }
            )
        }
        composable<Screen.Sistema> {
            val args = it.toRoute<Screen.Sistema>()
            SistemaScreen(
                sistemaId = args.sistemaId,
                goBack = {
                    navHostController.navigateUp()
                },
                isSistemaDelete = args.isSistemaDelete
            )
        }
        composable<Screen.ClienteList> {
            ClienteListScreen(
                createCliente = {
                    navHostController.navigate(Screen.Cliente(0, false))
                },
                onEditCliente = {
                    navHostController.navigate(Screen.Cliente(it, false))
                },
                onDeleteCliente = {
                    navHostController.navigate(Screen.Cliente(it, true))
                }
            )
        }
        composable<Screen.Cliente> {
            val args = it.toRoute<Screen.Cliente>()
            ClienteScreen(
                clienteId = args.clienteId,
                goBack = {
                    navHostController.navigateUp()
                },
                isClienteDelete = args.isClienteDelete
            )
        }
        composable<Screen.PrioridadList> {
            PrioridadListScreen(
                createPrioridad = {
                    navHostController.navigate(Screen.Prioridad(0, false))
                },
                onEditPrioridad = {
                    navHostController.navigate(Screen.Prioridad(it, false))
                },
                onDeletePrioridad = {
                    navHostController.navigate(Screen.Prioridad(it, true))
                }
            )
        }
        composable<Screen.Prioridad> {
            val args = it.toRoute<Screen.Prioridad>()
            PrioridadScreen(
                prioridadId = args.prioridadId,
                goBack = {
                    navHostController.navigateUp()
                },
                isPrioridadDelete = args.isPrioridadDelete
            )
        }
        composable<Screen.TicketList> {
            TicketListScreen(
                createTicket = {
                    navHostController.navigate(Screen.Ticket(0, false))
                },
                onEditTicket = {
                    navHostController.navigate(Screen.Ticket(it, false))
                },
                onDeleteTicket = {
                    navHostController.navigate(Screen.Ticket(it, true))
                }
            )
        }
        composable<Screen.Ticket> {
            val args = it.toRoute<Screen.Ticket>()
            TicketScreen(
                TicketId = args.ticketId,
                goBack = {
                    navHostController.navigateUp()
                },
                isTicketDelete = args.isTicketDelete
            )
        }
    }
}