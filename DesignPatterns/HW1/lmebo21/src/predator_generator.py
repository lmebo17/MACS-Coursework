from src.creature import BaseCreature
from src.evolution import Evolution
from src.physical_attributes_generator import PhysicalAttributesGenerator


class PredatorGenerator(PhysicalAttributesGenerator):
    def __init__(self, creature: BaseCreature):
        super().__init__(creature)

    def generate_location(self, min_bound: int, max_bound: int) -> int:
        location = 0
        self.creature.set_location(location)
        return location

    def generate_predator(self) -> None:
        self.generate_power(1, 10)
        self.generate_stamina(1000, 2000)
        self.generate_health(100, 200)
        self.generate_location(0, 0)

    @staticmethod
    def init_predator() -> BaseCreature:
        initial_predator = BaseCreature()
        predator_generator = PredatorGenerator(initial_predator)
        predator_generator.generate_predator()
        predator_instance = Evolution(initial_predator)
        initial_predator = predator_instance.evolve()
        initial_predator.set_name("Predator")
        return initial_predator
