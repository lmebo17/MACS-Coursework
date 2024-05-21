from src.creation_factory import CreationFactory


def test_manager_creation() -> None:
    factory = CreationFactory()
    manager = factory.create_manager()
    assert manager is not None


def test_cashier_creation() -> None:
    factory = CreationFactory()
    cashier = factory.create_cashier()
    assert cashier is not None


def test_customer_creation() -> None:
    factory = CreationFactory()
    customer = factory.create_customer([])
    assert customer is not None
