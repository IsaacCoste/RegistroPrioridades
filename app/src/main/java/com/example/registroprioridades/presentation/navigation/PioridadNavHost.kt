package com.example.registroprioridades.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.registroprioridades.presentation.home.HomeScreen
import com.example.registroprioridades.presentation.prioridad.PrioridadDeleteScreen
import com.example.registroprioridades.presentation.prioridad.PrioridadEditScreen
import com.example.registroprioridades.presentation.prioridad.PrioridadListScreen
import com.example.registroprioridades.presentation.prioridad.PrioridadScreen
import com.example.registroprioridades.presentation.ticket.TicketDeleteScreen
import com.example.registroprioridades.presentation.ticket.TicketEditScreen
import com.example.registroprioridades.presentation.ticket.TicketScreen
import com.example.registroprioridades.presentation.ticket.TicketListScreen

@Composable
fun PrioridadNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeScreen
    ) {
        composable<Screen.PrioridadList> {
            PrioridadListScreen(
                createPrioridad = {
                    navHostController.navigate(Screen.Prioridad(0))
                },
                onEditPrioridad = {
                    navHostController.navigate(Screen.PrioridadEdit(it))
                },
                onDeletePrioridad = {
                    navHostController.navigate(Screen.PrioridadDelete(it))
                }
            )
        }
        composable<Screen.HomeScreen>  {
            HomeScreen(navController = navHostController)
        }
        composable<Screen.TicketList> {
            TicketListScreen(
                createTicket = {
                    navHostController.navigate(Screen.Ticket(0))
                },
                onEditTicket = {
                    navHostController.navigate(Screen.TicketEdit(it))
                },
                onDeleteTicket = {
                    navHostController.navigate(Screen.TicketDelete(it))
                }
            )
        }
        composable<Screen.Prioridad> {
            val args = it.toRoute<Screen.Prioridad>()
            PrioridadScreen(
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
        composable<Screen.PrioridadEdit> {
            val args = it.toRoute<Screen.PrioridadEdit>()
            PrioridadEditScreen(
                prioridadId = args.prioridadId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
        composable<Screen.PrioridadDelete>{ backStackEntry ->
            val args = backStackEntry.toRoute<Screen.PrioridadDelete>()
            PrioridadDeleteScreen(
                prioridadId = args.prioridadId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
        composable<Screen.Ticket> {
            val args = it.toRoute<Screen.Ticket>()
            TicketScreen(
                TicketId = args.ticketId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
        composable<Screen.TicketEdit> {
            val args = it.toRoute<Screen.TicketEdit>()
            TicketEditScreen(
                ticketId = args.ticketId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
        composable<Screen.TicketDelete> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TicketDelete>()
            TicketDeleteScreen(
                ticketId = args.ticketId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}