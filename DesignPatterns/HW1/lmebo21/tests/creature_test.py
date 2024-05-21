from typing import Any

from src.creature import (
    BaseCreature,
    CreatureDecorator,
    CreatureWithClaw,
    CreatureWithTeeth,
)


def test_leg_count() -> None:
    creature = BaseCreature()
    assert creature.legs == 0


def test_set_leg_count() -> None:
    creature = BaseCreature()
    creature.set_legs(1)
    assert creature.get_legs() == 1


def test_wings_count() -> None:
    creature = BaseCreature()
    assert creature.get_wings() == 0


def test_set_wings_count() -> None:
    creature = BaseCreature()
    creature.set_wings(1)
    assert creature.get_wings() == 1


def test_get_power() -> None:
    creature = BaseCreature()
    assert creature.get_power() == 0


def test_set_power() -> None:
    creature = BaseCreature()
    creature.set_power(1)
    assert creature.get_power() == 1


def test_attack() -> None:
    creature = BaseCreature()
    creature.set_power(2)
    assert creature.attack() == 2


def test_health_count() -> None:
    creature = BaseCreature()
    creature.set_health(100)
    assert creature.get_health() == 100


def test_take_damage() -> None:
    creature = BaseCreature()
    creature.set_health(100)
    creature.take_damage(49)
    assert creature.get_health() == 51


def setup_creature(
    creature: BaseCreature, power: int, modifier: int, DecoratorClass: Any
) -> BaseCreature:
    creature.set_power(power)
    creature = DecoratorClass(CreatureDecorator(creature), modifier)
    return creature


def test_creature_with_claw() -> None:
    creature = setup_creature(BaseCreature(), 5, 2, CreatureWithClaw)
    assert creature.attack() == 5 * 2


def test_creature_with_teeth() -> None:
    creature = setup_creature(BaseCreature(), 5, 2, CreatureWithTeeth)
    assert creature.attack() == 5 + 2


def test_both() -> None:
    creature = setup_creature(BaseCreature(), 5, 2, CreatureWithTeeth)
    creature = setup_creature(creature, 5, 2, CreatureWithClaw)
    assert creature.attack() == (5 + 2) * 2
