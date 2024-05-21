from src.claws import Claws
from src.creature import BaseCreature
from src.pray_generator import PrayGenerator
from src.predator_generator import PredatorGenerator
from src.teeth import Teeth


def movement_simulation(predator: BaseCreature, pray: BaseCreature) -> bool:
    while predator.get_location() < pray.get_location():
        predator_step, _ = predator.move()
        pray_step, _ = pray.move()
        if predator_step == 0:
            return False
    return True


def fight_simulation(predator: BaseCreature, pray: BaseCreature) -> None:
    while predator.get_health() > 0 and pray.get_health() > 0:
        predator_given_damage = predator.attack()
        pray.take_damage(predator_given_damage)
        pray_given_damage = pray.attack()
        predator.take_damage(pray_given_damage)

    if pray.get_health() <= 0:
        print("Some R-rated things have happened\n")
    else:
        print("Pray ran into infinity\n")


def log(creature: BaseCreature) -> None:
    creature_info = [
        f"{creature.get_name()} Teeth Type: {Teeth.teeth[creature.get_teeth_type()]}",
        f"{creature.get_name()} Claw Type: {Claws.claws[creature.get_claw_type()]}",
        f"{creature.get_name()} Location: {str(creature.get_location())}",
        f"{creature.get_name()} Health: {str(creature.get_health())}",
        f"{creature.get_name()} Stamina: {str(creature.get_stamina())}",
        f"{creature.get_name()} Power: {str(creature.get_power())}",
        f"{creature.get_name()} Wings: {str(creature.get_wings())}",
        f"{creature.get_name()} Legs: {str(creature.get_legs())}\n",
    ]
    print("\n".join(creature_info))


if __name__ == "__main__":
    for _ in range(0, 100):
        predator = PredatorGenerator.init_predator()
        log(predator)
        pray = PrayGenerator.init_pray()
        log(pray)
        predator_caught_pray = movement_simulation(predator, pray)
        if predator_caught_pray:
            fight_simulation(predator, pray)
        else:
            print("Pray ran into infinity\n")
