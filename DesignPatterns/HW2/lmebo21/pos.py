import typer

from constants import NUMBER_OF_SHIFTS
from src.logger import Logger
from src.simulation import Simulation
from src.store import Store


def main(command: str):
    store = Store()

    if command == "simulate":
        for _ in range(NUMBER_OF_SHIFTS):
            simulation = Simulation(store)
            simulation.shift_simulation()
    elif command == "list":
        Logger.print_catalogue(store)
        Logger.print_discounts(store)
    elif command == "report":
        Logger.print_life_time_sold_items(store)
        Logger.print_life_time_revenue(store)



if __name__ == "__main__":
    typer.run(main)
