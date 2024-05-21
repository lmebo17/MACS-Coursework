from src import constants
from src.pray_generator import PrayGenerator
from src.predator_generator import PredatorGenerator


def test_predator_movement() -> None:
    predator = PredatorGenerator.init_predator()
    distance, _ = predator.move()
    assert predator.get_location() == distance


def test_pray_movement() -> None:
    pray = PrayGenerator.init_pray()
    starting_location = pray.get_location()
    distance, _ = pray.move()
    assert pray.get_location() == starting_location + distance


def test_stamina_change() -> None:
    pray = PrayGenerator.init_pray()
    starting_stamina = pray.get_stamina()
    _, stamina = pray.move()
    current_stamina = pray.get_stamina()
    assert current_stamina + stamina == starting_stamina


def test_flying_strategy() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_FLY)
    pray.set_wings(2)
    distance, stamina = pray.move()
    assert distance == constants.FLY_SPEED and stamina == constants.FLY_USED_STAMINA


def test_running_strategy() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_FLY - 1)
    pray.set_legs(2)
    distance, stamina = pray.move()
    assert distance == constants.RUN_SPEED and stamina == constants.RUN_USED_STAMINA


def test_walking_strategy() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_RUN - 1)
    pray.set_legs(2)
    distance, stamina = pray.move()
    assert distance == constants.WALK_SPEED and stamina == constants.WALK_USED_STAMINA


def test_hopping_strategy() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_WALK - 1)
    pray.set_legs(1)
    distance, stamina = pray.move()
    assert distance == constants.HOP_SPEED and stamina == constants.HOP_USED_STAMINA


def test_crawling_strategy() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_HOP - 1)
    pray.set_legs(0)
    distance, stamina = pray.move()
    assert (
        distance == constants.CRAWLING_SPEED
        and stamina == constants.CRAWLING_USED_STAMINA
    )


def test_insufficient_wings() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_FLY)
    pray.set_wings(0)
    pray.set_legs(2)
    distance, stamina = pray.move()
    assert distance == constants.RUN_SPEED and stamina == constants.RUN_USED_STAMINA


def test_insufficient_legs() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_RUN)
    pray.set_wings(0)
    pray.set_legs(1)
    distance, stamina = pray.move()
    assert distance == constants.HOP_SPEED and stamina == constants.HOP_USED_STAMINA


def test_insufficient_legs_and_wings() -> None:
    pray = PrayGenerator.init_pray()
    pray.set_stamina(constants.STAMINA_TO_RUN)
    pray.set_wings(0)
    pray.set_legs(0)
    distance, stamina = pray.move()
    assert (
        distance == constants.CRAWLING_SPEED
        and stamina == constants.CRAWLING_USED_STAMINA
    )
