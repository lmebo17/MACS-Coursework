import random
from typing import Any, List

from src.claws import Claws
from src.creature import (
    BaseCreature,
    CreatureDecorator,
    CreatureWithClaw,
    CreatureWithTeeth,
)
from src.teeth import Teeth


class Evolution:
    def __init__(self, creature: BaseCreature) -> None:
        self.creature = creature

    @staticmethod
    def random_boolean() -> bool:
        return random.choice([True, False])

    def evolve(self) -> BaseCreature:
        self.evolve_legs(0, 3)
        self.evolve_wings(0, 3)
        self.evolve_claws()
        self.evolve_teeth()
        return self.creature

    def evolve_creature(
        self, mod_choices: List[Any], DecoratorClass: Any
    ) -> BaseCreature:
        modifier = random.choice(mod_choices)
        if DecoratorClass == CreatureWithClaw:
            self.creature.set_claw_type(modifier)
        else:
            self.creature.set_teeth_type(modifier)
        self.add_feature(modifier, DecoratorClass)
        return self.creature

    def evolve_claws(self) -> BaseCreature:
        self.evolve_creature(Claws.claws_list(), CreatureWithClaw)
        return self.creature

    def evolve_teeth(self) -> BaseCreature:
        self.evolve_creature(Teeth.teeth_list(), CreatureWithTeeth)
        return self.creature

    def add_feature(self, mod: int, DecoratorClass: Any) -> None:
        self.creature = DecoratorClass(CreatureDecorator(self.creature), mod)

    def evolve_legs(self, min_bound: int, max_bound: int) -> BaseCreature:
        legs = random.randint(min_bound, max_bound)
        self.creature.set_legs(legs)
        return self.creature

    def evolve_wings(self, min_bound: int, max_bound: int) -> BaseCreature:
        wings = random.randint(min_bound, max_bound)
        self.creature.set_wings(wings)
        return self.creature
