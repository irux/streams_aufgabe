import unittest
from ad.agents import build_analytics_object, calc_fake_and_valid_percent


class TestUtilsAd(unittest.TestCase):

    def test_analytics_builder(self):
        build_object = build_analytics_object("test")
        self.assertTrue(build_object.camp_id == "test", "Camp id should be the same name")
        self.assertTrue(build_object.fake_clicks == 0, "The init value for fake clicks should be 0")
        self.assertTrue(build_object.valid_clicks == 0, "The init value for valid clicks should be 0")
        self.assertTrue(build_object.total == 0, "The init value for total clicks should be 0")
        self.assertTrue(build_object.valid_percent == 0.0, "The init value for valid percent should be 0.0")
        self.assertTrue(build_object.fraud_percent == 0.0, "The init value for fraud percent should be 00.0")

    def test_valid_percent(self):
        test_object = build_analytics_object("test")
        test_object.total = 2
        test_object.fake_clicks = 1
        test_object.valid_clicks = 1
        changed_object = calc_fake_and_valid_percent(test_object)
        print(changed_object.valid_percent)
        self.assertTrue(changed_object.fraud_percent == 50.0, "The fraud clicks should be the half of the total")
        self.assertTrue(changed_object.valid_percent == 50.0, "The valid clicks should be the half of the total")
