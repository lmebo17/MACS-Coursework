import random

from src.creature import BaseCreature


class PhysicalAttributesGenerator:
    def __init__(self, creature: BaseCreature):
        self.creature = creature

    def generate_health(self, min_bound: int, max_bound: int) -> int:
        health = random.randint(min_bound, max_bound)
        self.creature.set_health(health)
        return health

    def generate_stamina(self, min_bound: int, max_bound: int) -> int:
        stamina = random.randint(min_bound, max_bound)
        self.creature.set_stamina(stamina)
        return stamina

    def generate_location(self, min_bound: int, max_bound: int) -> int:
        return 0

    def generate_power(self, min_bound: int, max_bound: int) -> int:
        power = random.randint(min_bound, max_bound)
        self.creature.set_power(power)
        return power
