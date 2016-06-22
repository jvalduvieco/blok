package com.blok.Post.Functional

import com.blok.Application
import cucumber.api.junit.Cucumber
import org.junit.BeforeClass
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
class BlokFunctionalTest {
    companion object {
        @BeforeClass
        @JvmStatic fun setup() {
            Application().start(arrayOf())
        }
    }
}