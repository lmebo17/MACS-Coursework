from src.creature import BaseCreature
from src.evolution import Evolution

evolution_instance = Evolution(BaseCreature())


def test_evolve_legs() -> None:
    creature = evolution_instance.evolve_legs(1, 5)
    assert 1 <= creature.get_legs() <= 5


def test_evolve_wings() -> None:
    creature = evolution_instance.evolve_wings(1, 3)
    assert 1 <= creature.wings <= 3


def test_evolve_claws() -> None:
    creature = BaseCreature()
    flag = False
    for _ in range(0, 10):
        evolved_creature = evolution_instance.evolve_claws()
        flag |= creature != evolved_creature


def test_evolve_teeth() -> None:
    creature = BaseCreature()
    flag = False
    for _ in range(0, 10):
        evolved_creature = evolution_instance.evolve_teeth()
        flag |= creature != evolved_creature


def test_evolve() -> None:
    evolved_creature = evolution_instance.evolve()
    flag = True
    for _ in range(0, 10):
        flag &= 0 <= evolved_creature.get_legs() <= 5
        flag &= 0 <= evolved_creature.get_wings() <= 5
    assert flag
