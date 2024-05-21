from __future__ import annotations

from dataclasses import dataclass, field
from typing import Protocol
from uuid import UUID, uuid4


@dataclass
class UnitService:
    units: UnitRepository

    def create(self, unit: Unit) -> None:
        self.units.create(unit)

    def read(self, unit_id: UUID) -> Unit:
        return self.units.read(unit_id)

    def get_all(self) -> list[Unit]:
        return self.units.get_all()


class UnitRepository(Protocol):
    def create(self, unit: Unit) -> None:
        pass

    def read(self, unit_id: UUID) -> Unit:
        pass

    def get_all(self) -> list[Unit]:
        pass


@dataclass
class Unit:
    name: str
    id: UUID = field(default_factory=uuid4)
