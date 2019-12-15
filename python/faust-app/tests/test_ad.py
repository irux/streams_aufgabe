from ad import AdCampaignClickInfo
from app_faust import create_app
from unittest.mock import Mock, patch
from ad.agents import count_occurrence
from ad.agents import build_analytics_object
import json
import faust



async def run_test():
    app = create_app()
    app.conf.store = 'memory://'
    await counter_occurrence()


async def counter_occurrence():
    dict_data = {
        "camp_id": "test3",
        "cookie": "tests",
        "is_fake": 0
    }
    mock_object = AdCampaignClickInfo(**dict_data)

    async with count_occurrence.test_context() as agent:
        event1 = await agent.put(mock_object)
        result_object = agent.results[event1.message.offset]

        assert result_object.camp_id == mock_object.camp_id
        assert result_object.fake_clicks == 0
        assert result_object.total == 1
        assert result_object.valid_clicks == 1
        assert result_object.fraud_percent == 0.0
        assert result_object.valid_percent == 100.0
        # Repeat again to see the tracking effect
        mock_object.is_fake = 1
        event2 = await agent.put(mock_object)
        result_object = agent.results[event2.message.offset]
        assert result_object.camp_id == mock_object.camp_id
        assert result_object.fake_clicks == 1
        assert result_object.total == 2
        assert result_object.valid_clicks == 1
        assert result_object.fraud_percent == 50.0
        assert result_object.valid_percent == 50.0










def mock_coro(return_value=None, **kwargs):
    """Create mock coroutine function."""
    async def wrapped(*args, **kwargs):
        return return_value
    return Mock(wraps=wrapped, **kwargs)


if __name__ == '__main__':
    import asyncio
    loop = asyncio.get_event_loop()
    loop.run_until_complete(run_test())

