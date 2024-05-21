from dataclasses import dataclass, field
from typing import Tuple

from src.movement import GreedyStrategy, MovementStrategy


@dataclass
class BaseCreature:
    strategy: MovementStrategy = field(default_factory=GreedyStrategy)
    location: int = 0
    power: int = 0
    stamina: int = 0
    health: int = 0
    legs: int = 0
    wings: int = 0
    claw_type: int = 0
    teeth_type: int = 0
    name: str = ""

    def attack(self) -> int:
        return self.power

    def move(self) -> Tuple[int, int]:
        dist, stam = self.strategy.move(self.legs, self.wings, self.stamina)
        self.set_location(self.get_location() + dist)
        self.set_stamina(self.get_stamina() - stam)
        return dist, stam

    def set_name(self, name: str) -> None:
        self.name = name

    def get_name(self) -> str:
        return self.name

    def take_damage(self, damage: int) -> None:
        self.health -= damage

    def set_claw_type(self, claw: int) -> None:
        self.claw_type = claw

    def set_teeth_type(self, teeth: int) -> None:
        self.teeth_type = teeth

    def set_legs(self, legs: int) -> None:
        self.legs = legs

    def set_wings(self, wings: int) -> None:
        self.wings = wings

    def set_location(self, location: int) -> None:
        self.location = location

    def set_power(self, power: int) -> None:
        self.power = power

    def set_stamina(self, stamina: int) -> None:
        self.stamina = stamina

    def set_health(self, health: int) -> None:
        self.health = health

    def get_legs(self) -> int:
        return self.legs

    def get_wings(self) -> int:
        return self.wings

    def get_location(self) -> int:
        return self.location

    def get_power(self) -> int:
        return self.power

    def get_stamina(self) -> int:
        return self.stamina

    def get_health(self) -> int:
        return self.health

    def get_teeth_type(self) -> int:
        return self.teeth_type

    def get_claw_type(self) -> int:
        return self.claw_type


@dataclass
class CreatureDecorator:
    creature: BaseCreature

    def attack(self) -> int:
        return self.creature.attack()

    def move(self) -> Tuple[int, int]:
        return self.creature.move()

    def take_damage(self, damage: int) -> None:
        self.creature.take_damage(damage)

    def set_legs(self, legs: int) -> None:
        self.creature.set_legs(legs)

    def set_wings(self, wings: int) -> None:
        self.creature.set_wings(wings)

    def set_location(self, location: int) -> None:
        self.creature.set_location(location)

    def set_power(self, power: int) -> None:
        self.creature.set_power(power)

    def set_stamina(self, stamina: int) -> None:
        self.creature.set_stamina(stamina)

    def set_health(self, health: int) -> None:
        self.creature.set_health(health)

    def get_legs(self) -> int:
        return self.creature.get_legs()

    def get_wings(self) -> int:
        return self.creature.get_wings()

    def get_location(self) -> int:
        return self.creature.get_location()

    def get_power(self) -> int:
        return self.creature.get_power()

    def get_stamina(self) -> int:
        return self.creature.get_stamina()

    def get_health(self) -> int:
        return self.creature.get_health()

    def get_teeth_type(self) -> int:
        return self.creature.get_teeth_type()

    def get_claw_type(self) -> int:
        return self.creature.get_claw_type()

    def set_teeth_type(self, teeth: int) -> None:
        self.creature.set_teeth_type(teeth)

    def set_claw_type(self, claw: int) -> None:
        self.creature.set_claw_type(claw)

    def set_name(self, name: str) -> None:
        return self.creature.set_name(name)

    def get_name(self) -> str:
        return self.creature.get_name()


@dataclass
class CreatureWithClaw(CreatureDecorator):
    modifier: int

    def attack(self) -> int:
        return self.creature.attack() * self.modifier


@dataclass
class CreatureWithTeeth(CreatureDecorator):
    modifier: int

    def attack(self) -> int:
        return self.creature.attack() + self.modifier
