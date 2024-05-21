from dataclasses import dataclass, field
from uuid import UUID

from core.errors import DoesNotExistError
from core.unit import Unit


@dataclass
class UnitInMemory:
    units: dict[UUID, Unit] = field(default_factory=dict)

    def create(self, unit: Unit) -> None:
        self.units[unit.id] = unit

    def read(self, unit_id: UUID) -> Unit:
        try:
            unit = self.units[unit_id]
            return unit
        except KeyError:
            raise DoesNotExistError()

    def get_all(self) -> list[Unit]:
        return list(self.units.values())
