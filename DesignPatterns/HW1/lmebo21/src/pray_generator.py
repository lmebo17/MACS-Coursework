import random

from src.creature import BaseCreature
from src.evolution import Evolution
from src.physical_attributes_generator import PhysicalAttributesGenerator


class PrayGenerator(PhysicalAttributesGenerator):
    def __init__(self, creature: BaseCreature):
        super().__init__(creature)

    def generate_location(self, min_bound: int, max_bound: int) -> int:
        location = random.randint(min_bound, max_bound)
        self.creature.set_location(location)
        return location

    def generate_pray(self) -> None:
        self.generate_power(1, 10)
        self.generate_stamina(1000, 2000)
        self.generate_health(100, 200)
        self.generate_location(0, 1000)

    @staticmethod
    def init_pray() -> BaseCreature:
        initial_pray = BaseCreature()
        pray_generator = PrayGenerator(initial_pray)
        pray_generator.generate_pray()
        pray_instance = Evolution(initial_pray)
        initial_pray = pray_instance.evolve()
        initial_pray.set_name("Pray")
        return initial_pray
