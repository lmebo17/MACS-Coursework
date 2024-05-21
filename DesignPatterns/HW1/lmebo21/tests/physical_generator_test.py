from src.creature import BaseCreature
from src.evolution import Evolution
from src.pray_generator import PrayGenerator
from src.predator_generator import PredatorGenerator

evolution_instance = Evolution(BaseCreature())


def test_health() -> None:
    evolved_creature = evolution_instance.evolve()
    generator = PrayGenerator(evolved_creature)
    health = generator.generate_health(50, 100)
    assert evolved_creature.get_health() == health
    assert 50 <= health <= 100


def test_stamina() -> None:
    evolved_creature = evolution_instance.evolve()
    generator = PrayGenerator(evolved_creature)
    stamina = generator.generate_stamina(30, 40)
    assert evolved_creature.get_stamina() == stamina
    assert 30 <= stamina <= 40


def test_power() -> None:
    evolved_creature = evolution_instance.evolve()
    generator = PrayGenerator(evolved_creature)
    power = generator.generate_power(5, 10)
    assert evolved_creature.get_power() == power
    assert 5 <= power <= 10


def test_predator_generate_location() -> None:
    predator = BaseCreature()
    generator = PredatorGenerator(predator)
    location = generator.generate_location(0, 0)
    assert location == 0
    assert predator.get_location() == 0


def test_pray_generate_location() -> None:
    pray = BaseCreature()
    generator = PrayGenerator(pray)
    location = generator.generate_location(0, 1000)
    assert 0 <= pray.get_location() <= 1000
    assert location == pray.get_location()
